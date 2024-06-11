package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import java.io.File;
import java.net.URI;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processor;
  private CompilerInput input;
  private NodeFactory factory;
  private NodeOperations ops;

  @Before
  public void setup() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    processor = new ProcessCommonJSModules(compiler, "");
    input = mock(CompilerInput.class);
    when(input.getSourceFileName()).thenReturn("test.js");
    when(input.getSourceName()).thenReturn("src/test.js");
    factory = mock(NodeFactory.class);
    ops = mock(NodeOperations.class);
  }

  @Test
  public void testGuessCJSModuleName() {
    assertNotNull(processor.guessCJSModuleName("src/test.js"));
    assertNotNull(processor.guessCJSModuleName("src/test.js/sub"));
    assertNotNull(processor.guessCJSModuleName("src/sub/test.js"));
    assertNull(processor.guessCJSModuleName("test.js"));
    assertNull(processor.guessCJSModuleName("sub"));
  }

  @Test
  public void testToModuleName() {
    assertNotNull(ProcessorCommonJSModules.toModuleName("src/test.js"));
    assertNotNull(ProcessorCommonJSModules.toModuleName("src/test.js/sub"));
    assertNotNull(ProcessorCommonJSModules.toModuleName("src/sub/test.js"));
    assertNotNull(ProcessorCommonJSModules.toModuleName("test.js"));
    assertNotNull(ProcessorCommonJSModules.toModuleName("sub"));

    assertNotNull(ProcessorCommonJSModules.toModuleName("./src/test.js"));
    assertNotNull(ProcessorCommonJSModules.toModuleName("../sub/test.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertNotNull(processor.normalizeSourceName("src/test.js"));
    assertNotNull(processor.normalizeSourceName("src/test.js/sub"));
    assertNotNull(processor.normalizeSourceName("src/sub/test.js"));
    assertNull(processor.normalizeSourceName("test.js"));
    assertNull(processor.normalizeSourceName("sub"));

    assertNotNull(processor.normalizeSourceName("./src/test.js"));
    assertNotNull(processor.normalizeSourceName("../sub/test.js"));
  }

  @Test
  public void testProcess() {
    Node root = factory.parseTree("test = 1;");
    processor.process(input, root);

    Node moduleNameNode = root.getChildAtIndex(0);
    assertNotNull(moduleNameNode);
    assertNull(moduleNameNode.getQualifiedName());
    assertNull(moduleNameNode.getString());

    Node requireCall = root.getChildAtIndex(1);
    assertNotNull(requireCall);
    assertNotNull(requireCall.getChildAtIndex(1));

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertNotNull(moduleRef);
    assertNotNull(moduleRef.getQualifiedName());
    assertEquals("test", moduleRef.getQualifiedName());
    assertNull(moduleRef.getString());
  }

  @Test
  public void testProcessWithDependencies() {
    Node root = factory.parseTree("test = 1; require('dep');");
    processor = new ProcessCommonJSModules(mock(AbstractCompiler.class), "", true);
    processor.process(input, root);

    Node moduleNameNode = root.getChildAtIndex(0);
    assertNotNull(moduleNameNode);
    assertNull(moduleNameNode.getQualifiedName());
    assertNull(moduleNameNode.getString());

    Node requireCall = root.getChildAtIndex(1);
    assertNotNull(requireCall);
    assertNotNull(requireCall.getChildAtIndex(1));

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertNotNull(moduleRef);
    assertNotNull(moduleRef.getQualifiedName());
    assertEquals("dep", moduleRef.getQualifiedName());
    assertNull(moduleRef.getString());

    CompilerInput ci = input;
    Set<String> requires = ci.getRequires();
    assertNotNull(requires);
    assertEquals(1, requires.size());
    assertEquals("dep", requires.iterator().next());
  }
}
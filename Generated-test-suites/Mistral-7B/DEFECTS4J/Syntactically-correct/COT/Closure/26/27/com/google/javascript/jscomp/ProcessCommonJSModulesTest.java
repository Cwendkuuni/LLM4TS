import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.net.URI;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class ProcessCommonJSModulesTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private CompilerInput input;
  @Mock
  private NodeFactory nodeFactory;
  @InjectMocks
  private ProcessCommonJSModules processor;

  private Node rootNode;
  private CompilerInput inputWithScript;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    rootNode = new Node(SCRIPT, null);
    inputWithScript = new CompilerInput("test.js", new File("test.js"), nodeFactory);
    inputWithScript.setExterns(rootNode);
    inputWithScript.addInput(rootNode);

    when(compiler.getInput()).thenReturn(inputWithScript);
  }

  @Test
  public void testGuessCjsModuleName() {
    String filename = "path/to/myModule.js";
    String expected = "module$myModule";
    assertEquals(expected, processor.guessCJSModuleName(filename));
  }

  @Test
  public void testToModuleName() {
    String filename = "path/to/myModule.js";
    String expected = "module$myModule";
    String currentFilename = "path/to/anotherModule.js";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename, currentFilename));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "path/to/myModule.js";
    String expected = "myModule";
    assertEquals(expected, processor.normalizeSourceName(filename));
  }

  @Test
  public void testProcess() throws Exception {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);

    processor.process(inputWithScript.getExterns(), rootNode);

    Set<String> expectedProvides = Sets.newHashSet("myModule");
    Set<String> expectedRequires = Sets.newHashSet();

    verify(compiler).reportCodeChange();
    verify(inputWithScript).addProvide("myModule");
    verify(inputWithScript).addRequire("myModule");

    nodeTraversal.traverse(rootNode, new NodeVisitor());

    assertEquals(expectedProvides, nodeTraversal.getInput().getProvides());
    assertEquals(expectedRequires, nodeTraversal.getInput().getRequires());
  }

  private static class NodeVisitor extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName())) {
        String moduleName = processor.guessCJSModuleName(n.getChildAtIndex(1)
            .getString());
        Node moduleRef = IR.name(moduleName).srcref(n);
        ParentChildUtil.replaceChild(parent, n, moduleRef);
      }
    }
  }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import com.google.javascript.jscomp.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {
  
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processCommonJSModules;
  
  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    processCommonJSModules = new ProcessCommonJSModules(compiler, "");
  }
  
  @Test
  public void testProcess() {
    Node externs = IR.root();
    Node root = IR.root();
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    processCommonJSModules.process(externs, root);
    // Add assertions here
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test/File.js";
    String expected = "module$test$File";
    String result = processCommonJSModules.guessCJSModuleName(filename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testToModuleName() {
    String filename = "test/File.js";
    String expected = "module$test$File";
    String result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./module";
    String currentFilename = "test/File.js";
    String expected = "module$test$module";
    String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }
  
}
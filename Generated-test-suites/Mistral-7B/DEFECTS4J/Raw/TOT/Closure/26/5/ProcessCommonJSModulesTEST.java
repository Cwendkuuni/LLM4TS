import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJsModulesCallback;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.IR;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws URISyntaxException {
    String inputFilePath = "input/commonjsModule.js";
    String outputFilePath = "output/commonjsModule.js";

    // Given a valid input file
    File inputFile = new File(inputFilePath);
    CompilerInput input = new CompilerInput(inputFile);
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.QUIRKS);

    // When processing the input file with ProcessCommonJSModules
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        "", true); // reportDependencies = true
    processor.process(input, null);

    // Then the output file should be correctly processed
    File outputFile = new File(outputFilePath);
    Node root = input.getRootNode();
    Node expectedRoot = new IR().parse(new File("expected/commonjsModule.js")).getRootNode();
    assertEquals(expectedRoot, root);

    // And the JSModule object should be correctly initialized
    JSModule module = processor.getModule();
    Set<String> expectedDependencies = Sets.newHashSet("dep1.js", "dep2.js");
    Set<String> actualDependencies = module.getDependencies();
    assertEquals(expectedDependencies, actualDependencies);

    // When processing the input file with ProcessCommonJSModules and an error
    // in the input file
    inputFile = new File("input/commonjsModuleWithError.js");
    input = new CompilerInput(inputFile);
    processor = new ProcessCommonJSModules(compiler, "", true);
    try {
      processor.process(input, null);
      fail("Expected an error during processing");
    } catch (RuntimeException e) {
      // Then an error should be thrown
      String expectedErrorMessage = "Error in input file: Error: " +
          "Unexpected token '{'";
      String actualErrorMessage = e.getMessage();
      assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }
  }
}
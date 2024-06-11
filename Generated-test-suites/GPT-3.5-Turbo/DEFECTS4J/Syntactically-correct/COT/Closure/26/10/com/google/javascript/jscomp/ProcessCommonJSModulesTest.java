import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");

    // Test 1: Normal case
    String filename = "src/test.js";
    String expected = "module$test";
    String actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);

    // Test 2: Filename with leading "./"
    filename = "./src/test.js";
    expected = "module$test";
    actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);

    // Test 3: Filename with leading "../"
    filename = "../src/test.js";
    expected = "module$src$test";
    actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);

    // Test 4: Filename with trailing ".js"
    filename = "src/test.js";
    expected = "module$test";
    actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);

    // Test 5: Filename with "-"
    filename = "src/test-module.js";
    expected = "module$test_module";
    actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    // Test 1: Normal case
    String filename = "src/test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    // Test 2: Filename with leading "./"
    filename = "./src/test.js";
    expected = "module$test";
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    // Test 3: Filename with leading "../"
    filename = "../src/test.js";
    expected = "module$src$test";
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    // Test 4: Filename with trailing ".js"
    filename = "src/test.js";
    expected = "module$test";
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    // Test 5: Filename with "-"
    filename = "src/test-module.js";
    expected = "module$test_module";
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithCurrentFilename() {
    String requiredFilename;
    String currentFilename;
    String expected;
    String actual;

    // Test 1: Normal case
    requiredFilename = "src/test.js";
    currentFilename = "src/main.js";
    expected = "module$test";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);

    // Test 2: Required filename with leading "./"
    requiredFilename = "./src/test.js";
    currentFilename = "src/main.js";
    expected = "module$test";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);

    // Test 3: Required filename with leading "../"
    requiredFilename = "../src/test.js";
    currentFilename = "src/main.js";
    expected = "module$src$test";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);

    // Test 4: Required filename with trailing ".js"
    requiredFilename = "src/test.js";
    currentFilename = "src/main.js";
    expected = "module$test";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);

    // Test 5: Required filename with "-"
    requiredFilename = "src/test-module.js";
    currentFilename = "src/main.js";
    expected = "module$test_module";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }
}
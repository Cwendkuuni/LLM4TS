import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    // create an instance of ScopedAliases
    scopedAliases = new ScopedAliases();
  }

  @Test
  public void testProcess_ValidInput_ReturnsExpectedResult() {
    // arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "});";
    String expected = "goog.dom.createElement(goog.dom.TagName.DIV);";

    // act
    String result = scopedAliases.process(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_InvalidFormat_ThrowsError() {
    // arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "  return DIV;\n" +
                   "});";

    // act and assert
    assertThrows(Error.class, () -> {
      scopedAliases.process(input);
    });
  }

  @Test
  public void testProcess_NullInput_ReturnsEmptyString() {
    // arrange
    String input = null;
    String expected = "";

    // act
    String result = scopedAliases.process(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_EmptyInput_ReturnsEmptyString() {
    // arrange
    String input = "";
    String expected = "";

    // act
    String result = scopedAliases.process(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testHotSwapScript_ValidInput_ReturnsExpectedResult() {
    // arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "});";
    String expected = "goog.dom.createElement(goog.dom.TagName.DIV);";

    // act
    String result = scopedAliases.hotSwapScript(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testHotSwapScript_MissingScopeBlock_ReturnsSameInput() {
    // arrange
    String input = "var dom = goog.dom;\n" +
                   "var DIV = dom.TagName.DIV;\n" +
                   "dom.createElement(DIV);";
    String expected = input;

    // act
    String result = scopedAliases.hotSwapScript(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testHotSwapScript_NullInput_ReturnsEmptyString() {
    // arrange
    String input = null;
    String expected = "";

    // act
    String result = scopedAliases.hotSwapScript(input);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void testHotSwapScript_EmptyInput_ReturnsEmptyString() {
    // arrange
    String input = "";
    String expected = "";

    // act
    String result = scopedAliases.hotSwapScript(input);

    // assert
    assertEquals(expected, result);
  }
}
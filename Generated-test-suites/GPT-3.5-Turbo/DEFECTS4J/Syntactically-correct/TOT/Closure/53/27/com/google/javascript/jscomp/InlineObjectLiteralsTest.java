import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals iol;

  @Before
  public void setUp() {
    iol = new InlineObjectLiterals();
  }

  @After
  public void tearDown() {
    iol = null;
  }

  @Test
  public void testProcess_emptyObject() {
    // Arrange
    String input = "var obj = {};";
    String expected = "var obj = {};";

    // Act
    String actual = iol.process(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_singleAssignment() {
    // Arrange
    String input = "var obj = {key: 'value'};";
    String expected = "var obj$key = 'value';";

    // Act
    String actual = iol.process(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_multipleAssignments() {
    // Arrange
    String input = "var obj = {key1: 'value1', key2: 'value2'};";
    String expected = "var obj$key1 = 'value1';var obj$key2 = 'value2';";

    // Act
    String actual = iol.process(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_multipleUses() {
    // Arrange
    String input = "var obj = {key: 'value'}; console.log(obj.key); console.log(obj.key);";
    String expected = "var obj$key = 'value'; console.log(obj$key); console.log(obj$key);";

    // Act
    String actual = iol.process(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_invalidAssignments() {
    // Arrange
    String input = "var obj = {key1: 'value1', key2: obj.key1};";
    String expected = "var obj = {key1: 'value1', key2: obj.key1};";

    // Act
    String actual = iol.process(input);

    // Assert
    assertEquals(expected, actual);
  }
}
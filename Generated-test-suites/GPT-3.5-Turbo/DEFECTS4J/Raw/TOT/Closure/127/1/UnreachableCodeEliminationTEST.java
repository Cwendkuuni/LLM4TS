import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadCodeAfterReturn() {
    // Arrange
    String input = "if (x) { return; alert('unreachable'); }";
    String expected = "if (x) { return; }";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeWithNoOpStatements() {
    // Arrange
    String input = "a.b.MyClass.prototype.propertyName;";
    String expected = "";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeWithNoOpStatementsAndSideEffect() {
    // Arrange
    String input = "a.b.MyClass.prototype.propertyName; alert('side effect');";
    String expected = "alert('side effect');";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeInsideFunction() {
    // Arrange
    String input = "function test() { return; alert('unreachable'); }";
    String expected = "function test() { return; }";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeInsideNestedFunction() {
    // Arrange
    String input = "function outer() { function inner() { return; alert('unreachable'); } }";
    String expected = "function outer() { function inner() { return; } }";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeInsideSwitchStatement() {
    // Arrange
    String input = "switch (x) { case 1: return; alert('unreachable'); }";
    String expected = "switch (x) { case 1: return; }";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testRemoveDeadCodeWithTryCatch() {
    // Arrange
    String input = "try { return; alert('unreachable'); } catch (e) { alert('catch'); }";
    String expected = "try { return; } catch (e) { alert('catch'); }";
    UnreachableCodeElimination uce = new UnreachableCodeElimination();

    // Act
    String actual = uce.removeDeadCode(input);

    // Assert
    assertEquals(expected, actual);
  }

}
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.SourceFile;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    // Given
    AbstractCompiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // When
    SourceFile externs = new SourceFile("externs.js");
    SourceFile root = new SourceFile("root.js");
    pass.process(externs, root);

    // Then
    // Perform asserts on the result here.
  }

  // Test cases for individual methods

  @Test
  public void testMoveMethods() {
    // Given

    // When

    // Then
  }

  @Test
  public void testProcessWithNullDeepestCommonModuleRef() {
    // Given

    // When

    // Then
    // Assert that an error is reported when deepestCommonModuleRef is null.
  }

  @Test
  public void testProcessWithUnreferencedNameInfo() {
    // Given

    // When

    // Then
    // Assert that unreferenced name infos are skipped.
  }

  @Test
  public void testProcessWithMethodThatCannotBeMoved() {
    // Given

    // When

    // Then
    // Assert that methods that cannot be moved are left unchanged.
  }

  @Test
  public void testProcessWithMethodThatIsGetOrSet() {
    // Given

    // When

    // Then
    // Assert that get or set properties are left unchanged.
  }

  @Test
  public void testProcessWithClassNotInGlobalScope() {
    // Given

    // When

    // Then
    // Assert that methods of classes not in the global scope are left unchanged.
  }

  @Test(expected = IllegalStateException.class)
  public void testProcessWithErrorReported() {
    // Given

    // When

    // Then
    // Assert that processing throws an exception when an error is reported.
  }

  @Test
  public void testGenerateStubs() {
    // Given

    // When

    // Then
    // Assert that stub functions are generated for moved methods.
  }

  @Test
  public void testUnstubMethods() {
    // Given

    // When

    // Then
    // Assert that unstubbed method bodies are inserted into the deeper module.
  }

  @Test
  public void testHasGeneratedAnyIds() {
    // Given
    IdGenerator idGenerator = new IdGenerator();

    // When

    // Then
    // Assert that idGenerator reports true when ids have been generated.
  }

  @Test
  public void testNewId() {
    // Given
    IdGenerator idGenerator = new IdGenerator();

    // When
    int newId = idGenerator.newId();

    // Then
    // Assert that a new id is generated.
  }
}
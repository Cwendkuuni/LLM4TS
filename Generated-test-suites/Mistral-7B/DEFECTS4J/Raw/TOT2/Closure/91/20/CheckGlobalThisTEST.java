import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // ...
  }

  @Test
  public void testShouldTraverseNoJSDoc() {
    // ...
  }

  @Test
  public void testShouldTraverseConstructor() {
    // ...
  }

  @Test
  public void testShouldTraverseThis() {
    // ...
  }

  @Test
  public void testShouldTraversePrototype() {
    // ...
  }

  @Test
  public void testVisit() {
    // ...
  }

  @Test
  public void testVisitWithConstructors() {
    // ...
  }

  @Test
  public void testVisitWithThisAnnotation() {
    // ...
  }

  @Test
  public void testVisitWithPrototype() {
    // ...
  }

  @Test
  public void testVisitWithOverrideAnnotation() {
    // ...
  }

  @Test
  public void testVisitWithGetProperty() {
    // Test visiting a method with a get property inside another get property
  }

  @Test
  public void testVisitWithGetPropertyPrototype() {
    // Test visiting a method with a get property that accesses prototype
  }

  @Test
  public void testVisitWithFunctionAsProperty() {
    // Test visiting a method that is a property of another object
  }

  @Test
  public void testVisitWithFunctionLentToPrototype() {
    // Test visiting a method that is lent to a prototype
  }
}
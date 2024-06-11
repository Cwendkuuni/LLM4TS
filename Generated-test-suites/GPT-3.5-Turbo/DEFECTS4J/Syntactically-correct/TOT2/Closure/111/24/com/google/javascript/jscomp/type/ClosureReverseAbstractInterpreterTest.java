import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testConstruction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    assertNotNull(interpreter);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType arrayType = interpreter.restrictToArrayVisitor.visit(type);
    assertNotNull(arrayType);
    assertTrue(arrayType.isSubtype(getNativeType(ARRAY_TYPE)));
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSType nonArrayType = interpreter.restrictToNotArrayVisitor.visit(type);
    assertNotNull(nonArrayType);
    assertFalse(nonArrayType.isSubtype(getNativeType(ARRAY_TYPE)));
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSType objectType = interpreter.restrictToObjectVisitor.visit(type);
    assertNotNull(objectType);
    assertTrue(objectType.isObject());
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSType nonObjectType = interpreter.restrictToNotObjectVisitor.visit(type);
    assertNotNull(nonObjectType);
    assertFalse(nonObjectType.isObject());
  }

}
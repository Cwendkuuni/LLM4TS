import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Visitor<JSType> restrictToArrayVisitor = interpreter.getRestrictToArrayVisitor();

    // Typical use case
    JSType objectType = createObjectType();
    JSType arrayType = createArrayType();
    JSType restrictedType = restrictToArrayVisitor.visit(objectType);
    assertSame(arrayType, restrictedType);

    // Edge case
    JSType topType = createTopType();
    JSType nullType = createNullType();
    JSType undefinedType = createUndefinedType();
    restrictedType = restrictToArrayVisitor.visit(topType);
    assertNull(restrictedType);
    restrictedType = restrictToArrayVisitor.visit(nullType);
    assertNull(restrictedType);
    restrictedType = restrictToArrayVisitor.visit(undefinedType);
    assertNull(restrictedType);

    // Error scenario
    assertNull(restrictToArrayVisitor.visit(null));
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Visitor<JSType> restrictToNotArrayVisitor = interpreter.getRestrictToNotArrayVisitor();

    // Typical use case
    JSType objectType = createObjectType();
    JSType arrayType = createArrayType();
    JSType restrictedType = restrictToNotArrayVisitor.visit(objectType);
    assertSame(objectType, restrictedType);
    restrictedType = restrictToNotArrayVisitor.visit(arrayType);
    assertNull(restrictedType);

    // Edge case
    JSType topType = createTopType();
    JSType nullType = createNullType();
    JSType undefinedType = createUndefinedType();
    restrictedType = restrictToNotArrayVisitor.visit(topType);
    assertSame(topType, restrictedType);
    restrictedType = restrictToNotArrayVisitor.visit(nullType);
    assertNull(restrictedType);
    restrictedType = restrictToNotArrayVisitor.visit(undefinedType);
    assertNull(restrictedType);

    // Error scenario
    assertNull(restrictToNotArrayVisitor.visit(null));
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Visitor<JSType> restrictToObjectVisitor = interpreter.getRestrictToObjectVisitor();

    // Typical use case
    JSType objectType = createObjectType();
    JSType functionType = createFunctionType();
    JSType restrictedType = restrictToObjectVisitor.visit(objectType);
    assertSame(objectType, restrictedType);
    restrictedType = restrictToObjectVisitor.visit(functionType);
    assertSame(functionType, restrictedType);

    // Edge case
    JSType topType = createTopType();
    JSType nullType = createNullType();
    JSType undefinedType = createUndefinedType();
    JSType noObjectType = createNoObjectType();
    restrictedType = restrictToObjectVisitor.visit(topType);
    assertSame(noObjectType, restrictedType);
    restrictedType = restrictToObjectVisitor.visit(nullType);
    assertNull(restrictedType);
    restrictedType = restrictToObjectVisitor.visit(undefinedType);
    assertNull(restrictedType);

    // Error scenario
    assertNull(restrictToObjectVisitor.visit(null));
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Visitor<JSType> restrictToNotObjectVisitor = interpreter.getRestrictToNotObjectVisitor();

    // Typical use case
    JSType objectType = createObjectType();
    JSType functionType = createFunctionType();
    JSType restrictedType = restrictToNotObjectVisitor.visit(objectType);
    assertNull(restrictedType);
    restrictedType = restrictToNotObjectVisitor.visit(functionType);
    assertNull(restrictedType);

    // Edge case
    JSType topType = createTopType();
    JSType nullType = createNullType();
    JSType undefinedType = createUndefinedType();
    JSType noObjectType = createNoObjectType();
    restrictedType = restrictToNotObjectVisitor.visit(topType);
    assertNull(restrictedType);
    restrictedType = restrictToNotObjectVisitor.visit(nullType);
    assertNull(restrictedType);
    restrictedType = restrictToNotObjectVisitor.visit(undefinedType);
    assertNull(restrictedType);

    // Error scenario
    assertNull(restrictToNotObjectVisitor.visit(null));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FlowScope blindScope = createBlindScope();
    JSTypeRegistry typeRegistry = createTypeRegistry();

    // Typical use case
    Node condition = createCondition();
    boolean outcome = true;
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(preciserScope);

    // Error scenario
    preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(null, blindScope, outcome);
    assertNull(preciserScope);
  }

  private JSType createObjectType() {
    // Create and return an instance of ObjectType
    return new ObjectType();
  }

  private JSType createArrayType() {
    // Create and return an instance of ArrayType
    return new ArrayType();
  }

  private JSType createTopType() {
    // Create and return an instance of TopType
    return new TopType();
  }

  private JSType createNullType() {
    // Create and return an instance of NullType
    return new NullType();
  }

  private JSType createUndefinedType() {
    // Create and return an instance of UndefinedType
    return new UndefinedType();
  }

  private JSType createNoObjectType() {
    // Create and return an instance of NoObjectType
    return new NoObjectType();
  }

  private JSType createFunctionType() {
    // Create and return an instance of FunctionType
    return new FunctionType();
  }

  private FlowScope createBlindScope() {
    // Create and return an instance of FlowScope
    return new FlowScope();
  }

  private JSTypeRegistry createTypeRegistry() {
    // Create and return an instance of JSTypeRegistry
    return new JSTypeRegistry();
  }

  private Node createCondition() {
    // Create and return an instance of Node
    return new Node();
  }

}
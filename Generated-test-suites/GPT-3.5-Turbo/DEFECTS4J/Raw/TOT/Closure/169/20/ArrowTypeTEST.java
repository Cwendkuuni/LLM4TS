import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_returnTypeNotSubtype_returnFalse() {
    ArrowType arrowType1 = new ArrowType(createRegistry(), null, createType("number"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), null, createType("string"));
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testIsSubtype_parameterTypesNotSubtype_returnFalse() {
    Node parameters1 = createParameters("number", "string");
    Node parameters2 = createParameters("string", "number");
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testIsSubtype_optionalArgumentNotSubtype_returnFalse() {
    Node parameters1 = createParameters("number");
    Node parameters2 = createParameters("number", "string");
    parameters2.getFirstChild().setOptionalArg(true);
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testIsSubtype_variableArgumentsUnequal_returnFalse() {
    Node parameters1 = createParameters("number");
    Node parameters2 = createParameters("number", "string");
    parameters2.setVarArgs(true);
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testIsSubtype_variableArguments_missingReturnParameterType_returnTrue() {
    Node parameters1 = createParameters("number");
    Node parameters2 = createParameters();
    parameters2.setVarArgs(true);
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertTrue(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testEquals_returnTypeNotEqual_returnFalse() {
    ArrowType arrowType1 = new ArrowType(createRegistry(), null, createType("number"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), null, createType("string"));
    
    assertFalse(arrowType1.equals(arrowType2));
  }
  
  @Test
  public void testEquals_parameterTypesNotEqual_returnFalse() {
    Node parameters1 = createParameters("number", "string");
    Node parameters2 = createParameters("string", "number");
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertFalse(arrowType1.equals(arrowType2));
  }
  
  @Test
  public void testEquals_optionalArgumentsNotEqual_returnFalse() {
    Node parameters1 = createParameters("number");
    Node parameters2 = createParameters("number", "string");
    parameters2.getFirstChild().setOptionalArg(true);
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertFalse(arrowType1.equals(arrowType2));
  }
  
  @Test
  public void testHashCode_returnTypeEqual_returnTrue() {
    ArrowType arrowType1 = new ArrowType(createRegistry(), null, createType("number"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), null, createType("number"));
    
    assertTrue(arrowType1.hashCode() == arrowType2.hashCode());
  }
  
  @Test
  public void testHashCode_parameterTypesEqual_returnTrue() {
    Node parameters1 = createParameters("number", "string");
    Node parameters2 = createParameters("number", "string");
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertTrue(arrowType1.hashCode() == arrowType2.hashCode());
  }
  
  @Test
  public void testHashCode_optionalArgumentsEqual_returnTrue() {
    Node parameters1 = createParameters("number");
    Node parameters2 = createParameters("number");
    parameters2.getFirstChild().setOptionalArg(true);
    ArrowType arrowType1 = new ArrowType(createRegistry(), parameters1, createType("boolean"));
    ArrowType arrowType2 = new ArrowType(createRegistry(), parameters2, createType("boolean"));
    
    assertTrue(arrowType1.hashCode() == arrowType2.hashCode());
  }
  
  private JSTypeRegistry createRegistry() {
    // create and return a JSTypeRegistry object
    return null;
  }
  
  private JSType createType(String name) {
    // create and return a JSType object
    return null;
  }
  
  private Node createParameters(String... types) {
    // create and return a Node object representing parameters
    return null;
  }

}
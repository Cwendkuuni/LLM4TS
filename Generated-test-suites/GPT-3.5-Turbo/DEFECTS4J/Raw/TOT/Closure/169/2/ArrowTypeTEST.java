import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  
  private ArrowType arrowType;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    arrowType = new ArrowType(registry, null, null);
  }

  @Test
  public void testIsSubtype_WhenReturnTypeIsSubtype_ReturnsTrue() {
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.returnType = registry.createUnknownType();
    arrowType.returnType = registry.createObjectType();
    assertTrue(arrowType.isSubtype(otherArrowType));
  }
  
  @Test
  public void testIsSubtype_WhenReturnTypeIsNotSubtype_ReturnsFalse() {
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.returnType = registry.createObjectType();
    arrowType.returnType = registry.createUnknownType();
    assertFalse(arrowType.isSubtype(otherArrowType));
  }
  
  @Test
  public void testHasEqualParameters_WhenDifferentParameters_ReturnsFalse() {
    Node thisParam1 = new Node(Token.NAME, null);
    Node thisParam2 = new Node(Token.NAME, null);
    Node otherParam = new Node(Token.NAME, null);
    arrowType.parameters.addChildToFront(thisParam1);
    arrowType.parameters.addChildToFront(thisParam2);
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.parameters.addChildToFront(otherParam);
    assertFalse(arrowType.hasEqualParameters(otherArrowType, true));
  }
  
  @Test
  public void testHasEqualParameters_WhenEqualParameters_ReturnsTrue() {
    Node thisParam1 = new Node(Token.NAME, null);
    Node thisParam2 = new Node(Token.NAME, null);
    Node otherParam1 = new Node(Token.NAME, null);
    Node otherParam2 = new Node(Token.NAME, null);
    arrowType.parameters.addChildToFront(thisParam1);
    arrowType.parameters.addChildToFront(thisParam2);
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.parameters.addChildToFront(otherParam1);
    otherArrowType.parameters.addChildToFront(otherParam2);
    assertTrue(arrowType.hasEqualParameters(otherArrowType, true));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper_WhenNotEqual_ReturnsFalse() {
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.returnType = registry.createUnknownType();
    assertFalse(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper_WhenEqual_ReturnsTrue() {
    arrowType.returnType = registry.createUnknownType();
    ArrowType otherArrowType = new ArrowType(registry, null, null);
    otherArrowType.returnType = registry.createUnknownType();
    assertTrue(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }
  
  @Test
  public void testHashCode_WhenReturnTypeIsNull_ReturnsZero() {
    arrowType.returnType = null;      
    assertEquals(0, arrowType.hashCode());
  }
  
  @Test
  public void testHashCode_WhenReturnTypeIsUnknownType_ReturnsOne() {
    arrowType.returnType = registry.createUnknownType();
    assertEquals(1, arrowType.hashCode());
  }
  
  @Test
  public void testResolveInternal_WhenReturnTypeIsNull_ReturnsTheInstance() {
    JSType resolvedType = arrowType.resolveInternal(null, null);
    assertSame(arrowType, resolvedType);
  }
  
  @Test
  public void testResolveInternal_WhenReturnTypeIsNotNull_ResolvesReturnType() {
    JSType resolvedReturnType = registry.createObjectType();
    arrowType.returnType = resolvedReturnType;
    JSType resolvedType = arrowType.resolveInternal(null, null);
    assertSame(resolvedReturnType, resolvedType.returnType);
  }
  
  @Test
  public void testHasAnyTemplateInternal_WhenReturnTypeHasAnyTemplate_ReturnsTrue() {
    arrowType.returnType = registry.createTemplateType("T");
    assertTrue(arrowType.hasAnyTemplateInternal());
  }
  
  @Test
  public void testHasAnyTemplateInternal_WhenParametersHaveAnyTemplate_ReturnsTrue() {
    Node thisParam = new Node(Token.NAME, null);
    thisParam.setJSType(registry.createTemplateType("T"));
    arrowType.parameters.addChildToFront(thisParam);
    assertTrue(arrowType.hasAnyTemplateInternal());
  }
  
  @Test
  public void testHasAnyTemplateInternal_WhenNoTemplates_ReturnsFalse() {
    assertFalse(arrowType.hasAnyTemplateInternal());
  }
  
}
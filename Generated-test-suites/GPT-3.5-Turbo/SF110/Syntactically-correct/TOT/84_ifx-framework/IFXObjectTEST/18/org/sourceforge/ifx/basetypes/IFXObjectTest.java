import static org.junit.Assert.*;
import org.junit.*;

public class IFXObjectTest {
  
  @Test
  public void testEquals_NullObject_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = null;
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_ObjectsOfDifferentClass_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObjectChild obj2 = new IFXObjectChild();
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_ObjectsWithDifferentMemberValues_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    obj2.setMember1("test");
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_SameObject_ReturnsTrue() {
    // Arrange
    IFXObject obj = new IFXObject();
    
    // Act
    boolean result = obj.equals(obj);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testEquals_EqualObjects_ReturnsTrue() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testEquals_ArrayMemberWithDifferentSizes_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    obj2.setArrayMember(new IFXObject[] { new IFXObject(), new IFXObject() });
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_ArrayMemberWithDifferentElements_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    obj1.setArrayMember(new IFXObject[] { new IFXObject(), new IFXObject() });
    obj2.setArrayMember(new IFXObject[] { new IFXObject(), new IFXObject(), new IFXObject() });
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_ArrayMemberWithEqualElements_ReturnsTrue() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    IFXObject[] array = new IFXObject[] { new IFXObject(), new IFXObject() };
    obj1.setArrayMember(array);
    obj2.setArrayMember(array);
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testEquals_BaseTypeMemberWithDifferentValues_ReturnsFalse() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    obj2.setBaseTypeMember(new BaseType("test1"));
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testEquals_BaseTypeMemberWithEqualValues_ReturnsTrue() {
    // Arrange
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    BaseType baseType = new BaseType("test");
    obj1.setBaseTypeMember(baseType);
    obj2.setBaseTypeMember(baseType);
    
    // Act
    boolean result = obj1.equals(obj2);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testToString_NullObject_ReturnsEmptyString() {
    // Arrange
    IFXObject obj = null;
    
    // Act
    String result = obj.toString();
    
    // Assert
    assertEquals("", result);
  }
  
  @Test
  public void testToString_ObjectWithNoMembers_ReturnsStringRepresentation() {
    // Arrange
    IFXObject obj = new IFXObject();
    
    // Act
    String result = obj.toString();
    
    // Assert
    assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n}", result);
  }
  
  @Test
  public void testToString_ObjectWithMembers_ReturnsStringRepresentation() {
    // Arrange
    IFXObject obj = new IFXObject();
    obj.setBaseTypeMember(new BaseType("test"));
    obj.setArrayMember(new IFXObject[] { new IFXObject(), new IFXObject() });
    
    // Act
    String result = obj.toString();
    
    // Assert
    assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n  arrayMember = [\n    [0]:\n    org.sourceforge.ifx.basetypes.IFXObject = {\n    }\n, \n    [1]:\n    org.sourceforge.ifx.basetypes.IFXObject = {\n    }\n\n  ]\n  baseTypeMember = test\n}", result);
  }
}
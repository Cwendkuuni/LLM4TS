import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.EnumType;

public class NamedTypeTest {
  
  @Test
  public void testResolveInternal_ResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "test.Type";
    NamedType type = new NamedType(registry, reference, "", 0, 0);
    
    // Mock the resolution of the referenced type
    JSType referencedType = registry.getNativeObjectType("test.Type");
    
    // Set the referenced and resolved types
    type.setReferencedType(referencedType);
    type.setResolvedTypeInternal(referencedType);
    
    // Call resolveInternal() to see if the resolved type is returned
    JSType resolvedType = type.resolveInternal(new ErrorReporter(), null);
    
    // Verify that the resolved type is returned
    assertEquals(referencedType, resolvedType);
  }
  
  @Test
  public void testResolveInternal_UnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "test.Unknown";
    NamedType type = new NamedType(registry, reference, "", 0, 0);
    
    // Call resolveInternal() to see if the unresolved type is returned
    JSType unresolvedType = type.resolveInternal(new ErrorReporter(), null);
    
    // Verify that the unresolved type is returned
    assertEquals(registry.getNativeObjectType("Unknown"), unresolvedType);
  }
  
  @Test
  public void testResolveInternal_EnumElementType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "test.EnumType.Element";
    NamedType type = new NamedType(registry, reference, "", 0, 0);
    
    // Mock the resolution of the referenced EnumElementType
    ObjectType enumType = new EnumType(registry, "test.EnumType", "", 0, 0);
    JSType referencedType = new EnumElementType(enumType, type);
    
    // Set the referenced and resolved types
    type.setReferencedType(referencedType);
    type.setResolvedTypeInternal(referencedType);
    
    // Call resolveInternal() to see if the resolved type is returned
    JSType resolvedType = type.resolveInternal(new ErrorReporter(), null);
    
    // Verify that the resolved type is returned
    assertEquals(referencedType, resolvedType);
  }
}
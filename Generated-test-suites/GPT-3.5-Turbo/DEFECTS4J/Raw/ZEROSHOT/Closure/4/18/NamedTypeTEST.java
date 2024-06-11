import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.common.base.Predicate;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.EnumType;
import java.util.List;

public class NamedTypeTest {

  @Test
  public void testDefineProperty() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    String propertyName = "propertyName";
    JSType type = new JSType();
    boolean inferred = false;
    Node propertyNode = new Node();
    boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);
    assertTrue(result);
  }

  @Test
  public void testToStringHelper() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    boolean forAnnotations = true;
    String result = namedType.toStringHelper(forAnnotations);
    assertEquals("reference", result);
  }

  @Test
  public void testGetReferenceName() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    String result = namedType.getReferenceName();
    assertEquals("reference", result);
  }

  @Test
  public void testHasReferenceName() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    boolean result = namedType.hasReferenceName();
    assertTrue(result);
  }

  @Test
  public void testIsNamedType() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    boolean result = namedType.isNamedType();
    assertTrue(result);
  }

  @Test
  public void testIsNominalType() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    boolean result = namedType.isNominalType();
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    int result = namedType.hashCode();
    assertEquals(-355548670, result);
  }
  
  @Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    Predicate<JSType> validator = new Predicate<JSType>() {
      public boolean apply(JSType type) {
        return true;
      }
    };
    boolean result = namedType.setValidator(validator);
    assertTrue(result);
  }
}
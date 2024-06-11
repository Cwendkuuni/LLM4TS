import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testConstructorWithParametersAndReturnType() {
    Node parameters = new Node(Node.VARARGS, new Node(Node.NAME, "param1"), new Node(Node.NAME, "param2"));
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
  }

  @Test
  public void testConstructorWithoutParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertNotNull(arrowType);
  }

  @Test
  public void testConstructorWithReturnTypeInferred() {
    Node parameters = new Node(Node.NAME, "param1");
    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);
    assertNotNull(arrowType);
  }

  @Test
  public void testIsSubtypeWhenReturnTypesAreEqual() {
    Node parameters1 = new Node(Node.VARARGS, new Node(Node.NAME, "param1"), new Node(Node.NAME, "param2"));
    JSType returnType1 = registry.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = new Node(Node.VARARGS, new Node(Node.NAME, "param1"), new Node(Node.NAME, "param2"));
    JSType returnType2 = registry.createNumberType();
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWhenReturnTypesAreDifferent() {
    Node parameters1 = new Node(Node.NAME, "param1");
    JSType returnType1 = registry.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = new Node(Node.NAME, "param1");
    JSType returnType2 = registry.createStringType();
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  // Add more test cases for each method...

}
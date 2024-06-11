import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param1")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param2"))))
    );
    JSType returnType = new JSType(registry, "ReturnType");
    arrowType = new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testIsSubtype_WithDifferentReturnTypes_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))));
    JSType returnType = new JSType(registry, "DifferentReturnType");
    ArrowType otherArrowType = new ArrowType(registry, parameters, returnType);
    
    boolean result = arrowType.isSubtype(otherArrowType);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_WithDifferentNumberOfParameters_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param1")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param2")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param3"))))
    );
    JSType returnType = new JSType(registry, "ReturnType");
    ArrowType otherArrowType = new ArrowType(registry, parameters, returnType);
    
    boolean result = arrowType.isSubtype(otherArrowType);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_WithSameParametersAndReturnType_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param1")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param2"))))
    );
    JSType returnType = new JSType(registry, "ReturnType");
    ArrowType otherArrowType = new ArrowType(registry, parameters, returnType);
    
    boolean result = arrowType.isSubtype(otherArrowType);

    assertTrue(result);
  }

  @Test
  public void testHashCode_WithEqualParametersAndReturnType_ReturnsSameHash() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param1")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param2"))))
    );
    JSType returnType = new JSType(registry, "ReturnType");
    ArrowType otherArrowType = new ArrowType(registry, parameters, returnType);
    
    int hash1 = arrowType.hashCode();
    int hash2 = otherArrowType.hashCode();

    assertEquals(hash1, hash2);
  }

  @Test
  public void testHashCode_WithDifferentReturnType_ReturnsDifferentHash() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST,
        new Node(Token.BLOCK, new Node(Token.PARAMETER_LIST, new Node(Token.EMPTY))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param1")))),
        new Node(Token.PARAM_LIST,
            new Node(Token.BLOCK, new Node(Token.NAME, new Node(Token.STRING, "param2"))))
    );
    JSType returnType = new JSType(registry, "DifferentReturnType");
    ArrowType otherArrowType = new ArrowType(registry, parameters, returnType);
    
    int hash1 = arrowType.hashCode();
    int hash2 = otherArrowType.hashCode();

    assertNotEquals(hash1, hash2);
  }

  @Test
  public void testToStringHelper_WithForAnnotations_ReturnsExpectedString() {
    String expected = "[ArrowType]";

    String result = arrowType.toStringHelper(true);

    assertEquals(expected, result);
  }

  @Test
  public void testResolveInternal_WithNullParameters_ResolvesToNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = new JSType(registry, "ReturnType");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.resolveInternal(null, null);

    assertNull(arrowType.parameters);
  }

}
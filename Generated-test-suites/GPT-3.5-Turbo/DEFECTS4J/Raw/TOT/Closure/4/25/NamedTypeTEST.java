import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    }

    @Test
    public void testResolveInternal_WhenTypeIsResolvedViaRegistry_ShouldSetReferencedAndResolvedType() {
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());

        JSType expectedType = new JSType();
        expectedType.setName("reference");

        Assert.assertEquals(expectedType, namedType.getReferencedType());
        Assert.assertEquals(expectedType, namedType.getResolvedType());
    }

    @Test
    public void testResolveInternal_WhenTypeIsResolvedViaProperties_ShouldSetReferencedAndResolvedType() {
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());

        JSType expectedType = new JSType();
        expectedType.setName("resolvedType");

        Assert.assertEquals(expectedType, namedType.getReferencedType());
        Assert.assertEquals(expectedType, namedType.getResolvedType());
    }

    @Test
    public void testDefineProperty_WhenTypeIsResolved_ShouldDefineProperty() {
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());

        String propertyName = "propertyName";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();

        boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);
        Assert.assertTrue(result);
        Assert.assertEquals(type, namedType.getPropertyType(propertyName));
    }

    @Test
    public void testDefineProperty_WhenTypeIsNotResolved_ShouldNotDefineProperty() {
        String propertyName = "propertyName";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();

        boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);
        Assert.assertFalse(result);
        Assert.assertNull(namedType.getPropertyType(propertyName));
    }

}
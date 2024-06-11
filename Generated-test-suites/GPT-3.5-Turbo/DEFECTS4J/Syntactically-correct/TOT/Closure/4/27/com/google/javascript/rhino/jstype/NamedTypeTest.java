import org.junit.Assert;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testResolveInternal_ValidType_ResolvesCorrectly() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.example.Type", "Test.java", 10, 5);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());

        Assert.assertTrue(namedType.isResolved());
        Assert.assertTrue(namedType.getResolvedTypeInternal() instanceof JSType);
        Assert.assertEquals(namedType.getResolvedTypeInternal(), namedType.getReferencedType());
    }

    @Test
    public void testResolveInternal_UnknownType_ReportsWarning() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.example.UnknownType", "Test.java", 25, 10);
        ErrorReporter errorReporter = new ErrorReporter();
        namedType.resolveInternal(errorReporter, new StaticScope<>());

        Assert.assertTrue(namedType.isResolved());
        Assert.assertTrue(namedType.getResolvedTypeInternal() instanceof JSType);
        Assert.assertEquals(namedType.getResolvedTypeInternal(), namedType.getReferencedType());
        Assert.assertEquals(1, errorReporter.getWarningCount());
    }

    @Test
    public void testDefineProperty_UnresolvedType_PropertyContinuationAdded() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.example.UnknownType", "Test.java", 30, 5);
        namedType.defineProperty("property", new JSType(), false, new Node());

        Assert.assertFalse(namedType.isResolved());
        Assert.assertEquals(1, namedType.getPropertyContinuations().size());
        Assert.assertEquals("property", namedType.getPropertyContinuations().get(0).getPropertyName());
    }

    @Test
    public void testDefineProperty_ResolvedType_PropertyDefined() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.example.Type", "Test.java", 40, 10);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());

        namedType.defineProperty("property", new JSType(), false, new Node());

        Assert.assertTrue(namedType.isResolved());
        Assert.assertEquals(0, namedType.getPropertyContinuations().size());
        Assert.assertTrue(namedType.hasProperty("property"));
    }
}
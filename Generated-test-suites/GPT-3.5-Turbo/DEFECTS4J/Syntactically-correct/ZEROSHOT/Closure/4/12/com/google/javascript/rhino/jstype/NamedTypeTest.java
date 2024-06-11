import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp(){
        namedType = new NamedType(null, null, null, 0, 0);
    }

    @Test
    public void testDefineProperty(){
        String propertyName = "testProperty";
        JSType type = new JSType();
        boolean inferred = true;
        Node propertyNode = new Node();

        boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testDefineProperty_whenNotResolved(){
        namedType.setResolved(false);

        String propertyName = "testProperty";
        JSType type = new JSType();
        boolean inferred = true;
        Node propertyNode = new Node();

        boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetReferenceName(){
        String referenceName = namedType.getReferenceName();

        Assert.assertNull(referenceName);
    }

    @Test
    public void testToStringHelper(){
        String toString = namedType.toStringHelper(true);

        Assert.assertNotNull(toString);
    }

    @Test
    public void testHasReferenceName(){
        boolean hasReferenceName = namedType.hasReferenceName();

        Assert.assertTrue(hasReferenceName);
    }

    @Test
    public void testIsNamedType(){
        boolean isNamedType = namedType.isNamedType();

        Assert.assertTrue(isNamedType);
    }

    @Test
    public void testIsNominalType(){
        boolean isNominalType = namedType.isNominalType();

        Assert.assertTrue(isNominalType);
    }

    @Test
    public void testHashCode(){
        int hashCode = namedType.hashCode();

        Assert.assertNotEquals(0, hashCode);
    }

    @Test
    public void testResolveInternal(){
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();

        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

        Assert.assertNotNull(resolvedType);
    }

    @Test
    public void testResolveInternal_whenTypeCycleDetected(){
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        namedType.setReferencedAndResolvedType(namedType, errorReporter, enclosing);

        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

        Assert.assertNotNull(resolvedType);
    }

    @Test
    public void testGetTypedefType(){
        ErrorReporter errorReporter = new ErrorReporter();
        StaticSlot<JSType> slot = new StaticSlot<>("testSlot", new JSType());
        String name = "testName";

        JSType typedefType = namedType.getTypedefType(errorReporter, slot, name);

        Assert.assertNotNull(typedefType);
    }

    @Test
    public void testGetTypedefType_whenSlotTypeIsNull(){
        ErrorReporter errorReporter = new ErrorReporter();
        StaticSlot<JSType> slot = new StaticSlot<>("testSlot", null);
        String name = "testName";

        JSType typedefType = namedType.getTypedefType(errorReporter, slot, name);

        Assert.assertNull(typedefType);
    }

    @Test
    public void testGetTypedefType_whenUnresolvedTypeError(){
        ErrorReporter errorReporter = new ErrorReporter();
        StaticSlot<JSType> slot = new StaticSlot<>("testSlot", new JSType());
        String name = "testName";
        namedType.setResolved(false);

        JSType typedefType = namedType.getTypedefType(errorReporter, slot, name);

        Assert.assertNull(typedefType);
    }

    @Test
    public void testSetValidator(){
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return false;
            }
        };

        boolean result = namedType.setValidator(validator);

        Assert.assertTrue(result);
    }

}
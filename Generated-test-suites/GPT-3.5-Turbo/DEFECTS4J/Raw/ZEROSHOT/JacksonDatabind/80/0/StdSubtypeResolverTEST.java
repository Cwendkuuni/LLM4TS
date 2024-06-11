import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(TestClass1.class);
        NamedType type2 = new NamedType(TestClass2.class);
        NamedType type3 = new NamedType(TestClass3.class);
        
        subtypeResolver.registerSubtypes(type1, type2, type3);

        assertEquals(3, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(type1));
        assertTrue(subtypeResolver._registeredSubtypes.contains(type2));
        assertTrue(subtypeResolver._registeredSubtypes.contains(type3));
    }

    @Test
    public void testRegisterSubtypesClass() {
        Class<?> type1 = TestClass1.class;
        Class<?> type2 = TestClass2.class;
        Class<?> type3 = TestClass3.class;

        subtypeResolver.registerSubtypes(type1, type2, type3);

        assertEquals(3, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(type1)));
        assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(type2)));
        assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(type3)));
    }

    private class TestClass1 {}
    private class TestClass2 {}
    private class TestClass3 {}

    // Other test methods for collectAndResolveSubtypesByClass and collectAndResolveSubtypesByTypeId...
}
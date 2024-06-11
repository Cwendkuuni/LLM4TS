import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.*;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {
            new NamedType(TypeA.class),
            new NamedType(TypeB.class),
            new NamedType(TypeC.class)
        };
        subtypeResolver.registerSubtypes(types);

        assertEquals(types.length, subtypeResolver._registeredSubtypes.size());
        for (int i = 0; i < types.length; i++) {
            assertTrue(subtypeResolver._registeredSubtypes.contains(types[i]));
        }
    }

    @Test
    public void testRegisterSubtypesWithEmptyArray() {
        subtypeResolver.registerSubtypes(new NamedType[0]);

        assertNull(subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig config = new MapperConfig();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        assertNotNull(subtypes);
        // test typical use case
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeA.class)));

        // test edge case - empty baseType
        baseType = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeB.class)));

        // test edge case - empty registeredSubtypes
        subtypeResolver._registeredSubtypes = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedClass() {
        MapperConfig config = new MapperConfig();
        AnnotatedClass type = new AnnotatedClass();

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

        assertNotNull(subtypes);
        // test typical use case
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeA.class)));

        // test edge case - empty registeredSubtypes
        subtypeResolver._registeredSubtypes = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig config = new MapperConfig();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertNotNull(subtypes);
        // test typical use case
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeA.class)));

        // test edge case - empty baseType
        baseType = new JavaType();
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeB.class)));

        // test edge case - empty property
        property = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeB.class)));

        // test edge case - empty registeredSubtypes
        subtypeResolver._registeredSubtypes = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClass() {
        MapperConfig config = new MapperConfig();
        AnnotatedClass type = new AnnotatedClass();

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);

        assertNotNull(subtypes);
        // test typical use case
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(SubTypeA.class)));

        // test edge case - empty registeredSubtypes
        subtypeResolver._registeredSubtypes = null;
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    // additional test cases for comprehensive coverage can be added here

}
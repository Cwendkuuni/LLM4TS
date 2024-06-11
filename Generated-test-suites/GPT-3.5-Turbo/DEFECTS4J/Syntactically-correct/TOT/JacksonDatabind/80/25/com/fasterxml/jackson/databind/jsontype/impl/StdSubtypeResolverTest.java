import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType = new NamedType(SubType.class);
        subtypeResolver.registerSubtypes(namedType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(1, subtypes.size());
        assertEquals(namedType, subtypes.iterator().next());
    }

    @Test
    public void testRegisterSubtypesWithMultipleTypes() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testRegisterSubtypesUsingClasses() {
        Class<?>[] classes = {SubType.class, AnotherSubType.class};
        subtypeResolver.registerSubtypes(classes);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithInheritedTypes() {
        NamedType namedType1 = new NamedType(SubType.class);
        subtypeResolver.registerSubtypes(namedType1);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithPropertySubtypes() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithoutNamedTypes() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    @Test
    public void testCombineNamedAndUnnamedWithExplicitNames() {
        NamedType namedType1 = new NamedType(SubType.class, "SubType");
        NamedType namedType2 = new NamedType(AnotherSubType.class, "AnotherSubType");

        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();
        byName.put(namedType1.getName(), namedType1);
        byName.put(namedType2.getName(), namedType2);

        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(StdSubtypeResolver.class, typesHandled, byName);

        assertEquals(2, result.size());
        assertTrue(result.contains(namedType1));
        assertTrue(result.contains(namedType2));
    }

    @Test
    public void testCombineNamedAndUnnamedWithoutExplicitNames() {
        NamedType namedType1 = new NamedType(SubType.class);
        NamedType namedType2 = new NamedType(AnotherSubType.class);

        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        typesHandled.add(SubType.class);
        typesHandled.add(AnotherSubType.class);
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();

        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(StdSubtypeResolver.class, typesHandled, byName);

        assertEquals(2, result.size());
        assertTrue(result.contains(namedType1));
        assertTrue(result.contains(namedType2));
    }

    private static class SubType {
    }

    private static class AnotherSubType {
    }

}
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {
    
    private StdSubtypeResolver resolver;
    private MapperConfig<?> config;
    private AnnotatedClass annotatedClass;
    private AnnotatedMember annotatedMember;
    private JavaType baseType;
    
    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
        config = new MapperConfig<>();
        annotatedClass = new AnnotatedClass();
        annotatedMember = new AnnotatedMember();
        baseType = new JavaType();
    }
    
    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(DummySubtype1.class, "DummySubtype1");
        resolver.registerSubtypes(namedType1);
        
        // check if the namedType1 is added to registeredSubtypes
        assertTrue(resolver._registeredSubtypes.contains(namedType1));
        
        // check if the registeredSubtypes have size 1
        assertEquals(1, resolver._registeredSubtypes.size());
    }
    
    @Test
    public void testRegisterSubtypesWithMultipleTypes() {
        NamedType namedType1 = new NamedType(DummySubtype1.class, "DummySubtype1");
        NamedType namedType2 = new NamedType(DummySubtype2.class, "DummySubtype2");
        resolver.registerSubtypes(namedType1, namedType2);
        
        // check if the namedType1 and namedType2 are added to registeredSubtypes
        assertTrue(resolver._registeredSubtypes.contains(namedType1));
        assertTrue(resolver._registeredSubtypes.contains(namedType2));
        
        // check if the registeredSubtypes have size 2
        assertEquals(2, resolver._registeredSubtypes.size());
    }
    
    @Test
    public void testRegisterSubtypesWithClasss() {
        resolver.registerSubtypes(DummySubtype1.class, DummySubtype2.class);
        
        // check if the namedType1 and namedType2 are added to registeredSubtypes
        assertTrue(hasNamedType(resolver._registeredSubtypes, DummySubtype1.class));
        assertTrue(hasNamedType(resolver._registeredSubtypes, DummySubtype2.class));
        
        // check if the registeredSubtypes have size 2
        assertEquals(2, resolver._registeredSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        // Registering DummySubtype1 and DummySubtype2 as subtypes
        resolver.registerSubtypes(DummySubtype1.class, DummySubtype2.class);
        
        // Testing collectAndResolveSubtypesByClass for AnnotatedMember null Property
        Collection<NamedType> namedTypes1 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, namedTypes1.size());
        assertTrue(hasNamedType(namedTypes1, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes1, DummySubtype2.class));
        
        // Testing collectAndResolveSubtypesByClass with AnnotatedMember not null Property
        Collection<NamedType> namedTypes2 = resolver.collectAndResolveSubtypesByClass(config, annotatedMember, baseType);
        assertEquals(2, namedTypes2.size());
        assertTrue(hasNamedType(namedTypes2, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes2, DummySubtype2.class));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedClass() {
        // Registering DummySubtype1 and DummySubtype2 as subtypes
        resolver.registerSubtypes(DummySubtype1.class, DummySubtype2.class);
        
        // Testing collectAndResolveSubtypesByClass with AnnotatedClass
        Collection<NamedType> namedTypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        assertEquals(2, namedTypes.size());
        assertTrue(hasNamedType(namedTypes, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes, DummySubtype2.class));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        // Registering DummySubtype1 and DummySubtype2 as subtypes
        resolver.registerSubtypes(DummySubtype1.class, DummySubtype2.class);
        
        // Testing collectAndResolveSubtypesByTypeId for AnnotatedMember null Property
        Collection<NamedType> namedTypes1 = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, namedTypes1.size());
        assertTrue(hasNamedType(namedTypes1, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes1, DummySubtype2.class));
        
        // Testing collectAndResolveSubtypesByTypeId with AnnotatedMember not null Property
        Collection<NamedType> namedTypes2 = resolver.collectAndResolveSubtypesByTypeId(config, annotatedMember, baseType);
        assertEquals(2, namedTypes2.size());
        assertTrue(hasNamedType(namedTypes2, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes2, DummySubtype2.class));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClass() {
        // Registering DummySubtype1 and DummySubtype2 as subtypes
        resolver.registerSubtypes(DummySubtype1.class, DummySubtype2.class);
        
        // Testing collectAndResolveSubtypesByTypeId with AnnotatedClass
        Collection<NamedType> namedTypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        assertEquals(2, namedTypes.size());
        assertTrue(hasNamedType(namedTypes, DummySubtype1.class));
        assertTrue(hasNamedType(namedTypes, DummySubtype2.class));
    }
    
    @Test
    public void testCombineNamedAndUnnamed() {
        // Registering DummySubtype1 as Explicitly named type
        NamedType namedType = new NamedType(DummySubtype1.class, "DummySubtype1");
        resolver.registerSubtypes(namedType);
        
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        
        // Adding some unnamed classs to typesHandled set
        typesHandled.add(DummySubtype2.class);
        typesHandled.add(DummySubtype3.class);
        
        Collection<NamedType> combinedTypes = resolver._combineNamedAndUnnamed(DummyBase.class, typesHandled, byName);
        assertEquals(3, combinedTypes.size());
        assertTrue(hasNamedType(combinedTypes, DummySubtype1.class));
        assertTrue(hasNamedType(combinedTypes, DummySubtype2.class));
        assertTrue(hasNamedType(combinedTypes, DummySubtype3.class));
    }
    
    // Helper method to check if the namedType is present in the collection
    private boolean hasNamedType(Collection<NamedType> collection, Class<?> clazz) {
        for (NamedType namedType : collection) {
            if (namedType.getType() == clazz) {
                return true;
            }
        }
        return false;
    }
}
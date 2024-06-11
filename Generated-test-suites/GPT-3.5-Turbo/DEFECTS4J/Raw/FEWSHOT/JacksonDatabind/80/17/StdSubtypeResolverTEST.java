import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        NamedType[] types = {new NamedType(java.util.ArrayList.class), new NamedType(java.util.LinkedList.class)};
        subtypeResolver.registerSubtypes(types);
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
        
        Class<?>[] classs = {java.util.HashSet.class, java.util.TreeSet.class};
        subtypeResolver.registerSubtypes(classs);
        assertEquals(4, subtypeResolver._registeredSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = new MapperConfig<>();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();
        
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = new MapperConfig<>();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();
        
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertEquals(0, result.size());
    }
}
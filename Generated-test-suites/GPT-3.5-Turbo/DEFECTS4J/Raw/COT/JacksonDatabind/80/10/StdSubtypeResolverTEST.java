
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {
                new NamedType(String.class),
                new NamedType(Integer.class),
                new NamedType(Double.class)
        };

        resolver.registerSubtypes(types);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);

        assertEquals(3, subtypes.size());

        for (NamedType type : types) {
            assertTrue(subtypes.contains(type));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType[] types = {
                new NamedType(String.class),
                new NamedType(Integer.class),
                new NamedType(Double.class)
        };

        resolver.registerSubtypes(types);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null);

        assertEquals(3, subtypes.size());

        for (NamedType type : types) {
            assertTrue(subtypes.contains(type));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType[] types = {
                new NamedType(String.class),
                new NamedType(Integer.class),
                new NamedType(Double.class)
        };

        resolver.registerSubtypes(types);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);

        assertEquals(3, subtypes.size());

        for (NamedType type : types) {
            assertTrue(subtypes.contains(type));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithProperty() {
        NamedType[] types = {
                new NamedType(String.class),
                new NamedType(Integer.class),
                new NamedType(Double.class)
        };

        resolver.registerSubtypes(types);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(3, subtypes.size());

        for (NamedType type : types) {
            assertTrue(subtypes.contains(type));
        }
    }
}


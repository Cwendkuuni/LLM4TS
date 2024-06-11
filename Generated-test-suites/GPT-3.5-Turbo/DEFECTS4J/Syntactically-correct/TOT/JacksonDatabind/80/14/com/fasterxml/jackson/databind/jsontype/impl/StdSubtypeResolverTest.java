import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import java.util.*;
import java.lang.reflect.Modifier;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {

        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        NamedType namedType1 = new NamedType(ChildClass.class, "child");
        subtypeResovler.registerSubtypes(namedType1);
        Collection<NamedType> subtypes1 = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(1, subtypes1.size());
        assertEquals(namedType1, subtypes1.iterator().next());

        NamedType namedType2 = new NamedType(AnotherChildClass.class, "anotherChild");
        subtypeResolver.registerSubtypes(namedType2);
        Collection<NamedType> subtypes2 = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(2, subtypes2.size());
        assertTrue(subtypes2.contains(namedType1));
        assertTrue(subtypes2.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {

        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember member = null;
        JavaType baseType = null;

        subtypeResolver.registerSubtypes(ChildClass.class, AnotherChildClass.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, member, baseType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(ChildClass.class)));
        assertTrue(subtypes.contains(new NamedType(AnotherChildClass.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {

        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember member = null;
        JavaType baseType = null;

        subtypeResolver.registerSubtypes(ChildClass.class, AnotherChildClass.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, member, baseType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(ChildClass.class)));
        assertTrue(subtypes.contains(new NamedType(AnotherChildClass.class)));
    }

    private static class ChildClass {}
    private static class AnotherChildClass {}

}
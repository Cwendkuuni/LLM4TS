import static org.junit.Assert.*;
import org.junit.Test;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType type1 = new NamedType(String.class, "String");
        NamedType type2 = new NamedType(Integer.class, "Integer");

        resolver.registerSubtypes(type1, type2);

        assertTrue(resolver.getRegisteredSubtypes().contains(type1));
        assertTrue(resolver.getRegisteredSubtypes().contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // create necessary objects for testing
        // perform the method call

        // assert the expected result
        // assertTrue(...)
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // create necessary objects for testing
        // perform the method call

        // assert the expected result
        // assertTrue(...)
    }

    // add more test cases for other methods as needed

}
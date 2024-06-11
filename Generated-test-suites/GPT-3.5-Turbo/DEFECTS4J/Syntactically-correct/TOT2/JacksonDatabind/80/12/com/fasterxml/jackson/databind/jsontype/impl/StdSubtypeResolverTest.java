import static org.junit.Assert.*;
import org.junit.Test;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType type1 = new NamedType(SomeClass1.class);
        NamedType type2 = new NamedType(SomeClass2.class);
        
        resolver.registerSubtypes(type1, type2);
        
        assertTrue(resolver.getRegisteredSubtypes().contains(type1));
        assertTrue(resolver.getRegisteredSubtypes().contains(type2));
    }

    // Add more test cases for other public methods in StdSubtypeResolver class
    
}
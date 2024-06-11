import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object[].class, bindings);
        JavaType elemT = JavaType.construct(String.class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, elemT);
        assertEquals("collection type; class List<java.lang.String>, contains java.lang.String", collectionType.toString());
    }
}
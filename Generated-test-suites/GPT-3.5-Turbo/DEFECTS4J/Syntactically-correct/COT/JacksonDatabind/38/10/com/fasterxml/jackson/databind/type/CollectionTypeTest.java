
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, null, null, null, true);
        assertEquals("[collection type; class java.util.ArrayList, contains null]", collectionType.toString());
    }
}

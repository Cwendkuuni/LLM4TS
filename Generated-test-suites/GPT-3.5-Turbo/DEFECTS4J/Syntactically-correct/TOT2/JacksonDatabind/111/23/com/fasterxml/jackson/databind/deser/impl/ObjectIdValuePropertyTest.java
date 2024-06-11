import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = objectIdValueProperty.withNullProvider(nva);

        assertNotNull(result);
        assertEquals(nva, result.getNullProvider());
    }

    // Additional test cases for other public methods in ObjectIdValueProperty class can be added here
}
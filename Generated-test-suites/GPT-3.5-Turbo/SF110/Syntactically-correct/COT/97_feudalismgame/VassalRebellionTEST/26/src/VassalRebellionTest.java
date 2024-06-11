import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_emptyCollection() {
        Collection<String> emptyCollection = new ArrayList<>();
        vassalRebellion.perform(emptyCollection);
        
        // Add assertions to verify the expected behavior
    }

    @Test
    public void testPerform_collectionWithNull() {
        Collection<String> collectionWithNull = new ArrayList<>();
        collectionWithNull.add(null);
        vassalRebellion.perform(collectionWithNull);
        
        // Add assertions to verify the expected behavior
    }

    @Test
    public void testPerform_mapReturnsNull() {
        Map.map.clear();
        Collection<String> collection = new ArrayList<>();
        collection.add("fiefdomName");
        vassalRebellion.perform(collection);
        
        // Add assertions to verify the expected behavior
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_exceptionInGetName() {
        Map.map.put("fiefdomName", new Fiefdoms());
        Collection<String> collection = new ArrayList<>();
        collection.add("fiefdomName");
        vassalRebellion.perform(collection);
        
        // Exception is expected, no assertions needed
    }
}
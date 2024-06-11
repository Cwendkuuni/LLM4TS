import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test when both o1 and o2 are null
        int result = comparator.compare(null, null);
        assertEquals(0, result);
        
        // Test when o1 is null and o2 is not null
        result = comparator.compare(null, new AttributeModel());
        assertEquals(-1, result);
        
        // Test when o1 is not null and o2 is null
        result = comparator.compare(new AttributeModel(), null);
        assertEquals(1, result);
        
        // Test when both o1 and o2 have null names
        result = comparator.compare(new AttributeModel(null), new AttributeModel(null));
        assertEquals(0, result);
        
        // Test when both o1 and o2 have empty names
        result = comparator.compare(new AttributeModel(""), new AttributeModel(""));
        assertEquals(0, result);
        
        // Test when o1 has null name and o2 has non-null name
        result = comparator.compare(new AttributeModel(null), new AttributeModel("test"));
        assertEquals(-1, result);
        
        // Test when o1 has non-null name and o2 has null name
        result = comparator.compare(new AttributeModel("test"), new AttributeModel(null));
        assertEquals(1, result);
        
        // Test when o1 and o2 have different names and neither name contains "a"
        result = comparator.compare(new AttributeModel("abc"), new AttributeModel("def"));
        assertEquals(-1, result);
        
        // Test when o1 name contains "a" and o2 name does not contain "a"
        result = comparator.compare(new AttributeModel("abc"), new AttributeModel("defg"));
        assertEquals(1, result);
        
        // Test when o1 name does not contain "a" and o2 name contains "a"
        result = comparator.compare(new AttributeModel("defg"), new AttributeModel("abc"));
        assertEquals(-1, result);
        
        // Test when o1 and o2 have the same names, both containing "a"
        result = comparator.compare(new AttributeModel("abc"), new AttributeModel("abc"));
        assertEquals(0, result);
        
        // Test when o1 and o2 have the same names, both not containing "a"
        result = comparator.compare(new AttributeModel("xyz"), new AttributeModel("xyz"));
        assertEquals(0, result);
    }
}
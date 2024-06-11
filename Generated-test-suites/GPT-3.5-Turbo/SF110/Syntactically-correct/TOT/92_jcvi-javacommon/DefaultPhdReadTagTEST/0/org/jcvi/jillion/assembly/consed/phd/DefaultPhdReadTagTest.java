import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag defaultPhdReadTag;
    
    @Before
    public void setup() {
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", ungappedRange, date, "Comment1", "FreeForm1");
    }
    
    @After
    public void cleanup() {
        defaultPhdReadTag = null;
    }
    
    @Test
    public void testGetType() {
        assertEquals("Type1", defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        assertEquals("Source1", defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range expectedRange = new Range(0, 10);
        Range actualRange = defaultPhdReadTag.getUngappedRange();
        assertNotNull(actualRange);
        assertEquals(expectedRange.getStart(), actualRange.getStart());
        assertEquals(expectedRange.getEnd(), actualRange.getEnd());
    }
    
    @Test
    public void testGetDate() {
        Date expectedDate = new Date();
        Date actualDate = defaultPhdReadTag.getDate();
        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testGetComment() {
        assertEquals("Comment1", defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        assertEquals("FreeForm1", defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag otherDefaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1",
                new Range(0, 10), new Date(), "Comment1", "FreeForm1");
        assertEquals(defaultPhdReadTag.hashCode(), otherDefaultPhdReadTag.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag otherDefaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1",
                new Range(0, 10), new Date(), "Comment1", "FreeForm1");
        assertTrue(defaultPhdReadTag.equals(otherDefaultPhdReadTag));
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag otherDefaultPhdReadTag = new DefaultPhdReadTag("Type2", "Source2",
                new Range(10, 20), new Date(), "Comment2", "FreeForm2");
        assertFalse(defaultPhdReadTag.equals(otherDefaultPhdReadTag));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        assertFalse(defaultPhdReadTag.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClassObject() {
        assertFalse(defaultPhdReadTag.equals("String"));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
    }
}
import org.jcvi.jillion.assembly.consed.phd.*;
import org.jcvi.jillion.core.Range;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag phdReadTag;
    
    // Initialize test data
    @Before
    public void setUp() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        
        phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    // Test getType() method - typical use case
    @Test
    public void testGetType_typical() {
        String expectedResult = "type";
        String actualResult = phdReadTag.getType();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test getSource() method - typical use case
    @Test
    public void testGetSource_typical() {
        String expectedResult = "source";
        String actualResult = phdReadTag.getSource();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test getUngappedRange() method - typical use case
    @Test
    public void testGetUngappedRange_typical() {
        Range expectedResult = new Range(0, 10);
        Range actualResult = phdReadTag.getUngappedRange();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test getDate() method - typical use case
    @Test
    public void testGetDate_typical() {
        Date expectedResult = new Date();
        Date actualResult = phdReadTag.getDate();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test getComment() method - typical use case
    @Test
    public void testGetComment_typical() {
        String expectedResult = "comment";
        String actualResult = phdReadTag.getComment();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test getFreeFormData() method - typical use case
    @Test
    public void testGetFreeFormData_typical() {
        String expectedResult = "free form data";
        String actualResult = phdReadTag.getFreeFormData();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test hashCode() method - typical use case
    @Test
    public void testHashCode_typical() {
        int expectedResult = 123456;
        int actualResult = phdReadTag.hashCode();
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test equals() method - typical use case
    @Test
    public void testEquals_typical() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free form data");
        boolean expectedResult = true;
        boolean actualResult = phdReadTag.equals(other);
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test equals() method - different comment
    @Test
    public void testEquals_differentComment() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "different comment", "free form data");
        boolean expectedResult = false;
        boolean actualResult = phdReadTag.equals(other);
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test equals() method - null comment in other object
    @Test
    public void testEquals_nullCommentInOtherObject() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), null, "free form data");
        boolean expectedResult = false;
        boolean actualResult = phdReadTag.equals(other);
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test equals() method - null object
    @Test
    public void testEquals_nullObject() {
        DefaultPhdReadTag other = null;
        boolean expectedResult = false;
        boolean actualResult = phdReadTag.equals(other);
        
        assertEquals(expectedResult, actualResult);
    }
    
    // Test equals() method - different object type
    @Test
    public void testEquals_differentObjectType() {
        Object other = new Object();
        boolean expectedResult = false;
        boolean actualResult = phdReadTag.equals(other);
        
        assertEquals(expectedResult, actualResult);
    }
}
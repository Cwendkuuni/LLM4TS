import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    // Test case for asPostParameterList method
    
    @Test
    public void testAsPostParameterList_typicalCase() {
        Paging paging = new Paging(1, 10, 100, 200);
        List<HttpParameter> expected = new ArrayList<>();
        expected.add(new HttpParameter("since_id", "100"));
        expected.add(new HttpParameter("max_id", "200"));
        expected.add(new HttpParameter("count", "10"));
        expected.add(new HttpParameter("page", "1"));
        
        List<HttpParameter> actual = paging.asPostParameterList();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAsPostParameterList_emptyParameters() {
        Paging paging = new Paging();
        List<HttpParameter> expected = new ArrayList<>();
        
        List<HttpParameter> actual = paging.asPostParameterList();
        
        assertEquals(expected, actual);
    }
    
    // Test case for asPostParameterArray method
    
    @Test
    public void testAsPostParameterArray_typicalCase() {
        Paging paging = new Paging(1, 10, 100, 200);
        HttpParameter[] expected = {
            new HttpParameter("since_id", "100"),
            new HttpParameter("max_id", "200"),
            new HttpParameter("count", "10"),
            new HttpParameter("page", "1")
        };
        
        HttpParameter[] actual = paging.asPostParameterArray();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAsPostParameterArray_emptyParameters() {
        Paging paging = new Paging();
        HttpParameter[] expected = {};
        
        HttpParameter[] actual = paging.asPostParameterArray();
        
        assertArrayEquals(expected, actual);
    }
    
    // Test case for setPage method
    
    @Test
    public void testSetPage_validPage() {
        Paging paging = new Paging();
        
        paging.setPage(5);
        
        assertEquals(5, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_negativePage() {
        Paging paging = new Paging();
        
        paging.setPage(-5);
    }
    
    // Test case for setCount method
    
    @Test
    public void testSetCount_validCount() {
        Paging paging = new Paging();
        
        paging.setCount(20);
        
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_negativeCount() {
        Paging paging = new Paging();
        
        paging.setCount(-20);
    }
    
    // Test case for setSinceId method
    
    @Test
    public void testSetSinceId_validSinceId() {
        Paging paging = new Paging();
        
        paging.setSinceId(1000);
        
        assertEquals(1000, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_negativeSinceId() {
        Paging paging = new Paging();
        
        paging.setSinceId(-1000);
    }
    
    // Test case for setMaxId method
    
    @Test
    public void testSetMaxId_validMaxId() {
        Paging paging = new Paging();
        
        paging.setMaxId(500);
        
        assertEquals(500, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_negativeMaxId() {
        Paging paging = new Paging();
        
        paging.setMaxId(-500);
    }
    
    // Test case for equals method
    
    @Test
    public void testEquals_equalObjects() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(1, 10, 100, 200);
        
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_notEqualObjects() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(2, 10, 100, 200);
        
        assertFalse(paging1.equals(paging2));
    }
    
    // Test case for hashCode method
    
    @Test
    public void testHashCode_equalObjects() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(1, 10, 100, 200);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCode_notEqualObjects() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(2, 10, 100, 200);
        
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    // Test case for toString method
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 100, 200);
        
        String expected = "Paging{page=1, count=10, sinceId=100, maxId=200}";
        
        assertEquals(expected, paging.toString());
    }
}
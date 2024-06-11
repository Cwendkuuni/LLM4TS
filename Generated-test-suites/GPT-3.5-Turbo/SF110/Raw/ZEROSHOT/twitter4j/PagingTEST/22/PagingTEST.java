import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {
    
    @Test
    public void testPage_GetPage_ReturnsPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        int page = paging.getPage();
        
        assertEquals(2, page);
    }
    
    @Test
    public void testCount_GetCount_ReturnsCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        int count = paging.getCount();
        
        assertEquals(10, count);
    }
    
    @Test
    public void testSinceId_GetSinceId_ReturnsSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1000);
        long sinceId = paging.getSinceId();
        
        assertEquals(1000, sinceId);
    }
    
    @Test
    public void testMaxId_GetMaxId_ReturnsMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(2000);
        long maxId = paging.getMaxId();
        
        assertEquals(2000, maxId);
    }
    
    @Test
    public void testPage_SetPage_UpdatesPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        
        assertEquals(3, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPage_SetPageWithNegativeValue_ThrowsIllegalArgumentException() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testCount_SetCount_UpdatesCount() {
        Paging paging = new Paging();
        paging.setCount(20);
        
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCount_SetCountWithNegativeValue_ThrowsIllegalArgumentException() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSinceId_SetSinceId_UpdatesSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(3000);
        
        assertEquals(3000, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSinceId_SetSinceIdWithNegativeValue_ThrowsIllegalArgumentException() {
        Paging paging = new Paging();
        paging.setSinceId(-1);
    }
    
    @Test
    public void testMaxId_SetMaxId_UpdatesMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(4000);
        
        assertEquals(4000, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMaxId_SetMaxIdWithNegativeValue_ThrowsIllegalArgumentException() {
        Paging paging = new Paging();
        paging.setMaxId(-1);
    }
    
    @Test
    public void testHashCode_EqualsAndHashCodeAreConsistent() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 1000, 2000);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        Paging paging = new Paging();
        assertTrue(paging.equals(paging));
    }
    
    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        Paging paging = new Paging();
        assertFalse(paging.equals(new Object()));
    }
    
    @Test
    public void testEquals_EqualObjects_ReturnsTrue() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 1000, 2000);
        
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_DifferentPage_ReturnsFalse() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(2, 10, 1000, 2000);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_DifferentCount_ReturnsFalse() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 20, 1000, 2000);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_DifferentSinceId_ReturnsFalse() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 2000, 2000);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_DifferentMaxId_ReturnsFalse() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 1000, 3000);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testToString_ReturnsStringRepresentation() {
        Paging paging = new Paging(2, 20, 1000, 2000);
        String expected = "Paging{page=2, count=20, sinceId=1000, maxId=2000}";
        
        assertEquals(expected, paging.toString());
    }
}
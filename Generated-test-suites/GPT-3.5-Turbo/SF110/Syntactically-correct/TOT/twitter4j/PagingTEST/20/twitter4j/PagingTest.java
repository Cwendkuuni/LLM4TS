
package twitter4j;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {
    
    private Paging paging;
    
    @Before
    public void setup() {
        paging = new Paging();
    }
    
    // Test cases for 'asPostParameterList' method
    
    @Test
    public void asPostParameterList_shouldReturnEmptyList_whenNoSupportedParams() {
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        
        assertEquals(expected, paging.asPostParameterList(new char[] {}));
    }
    
    @Test
    public void asPostParameterList_shouldReturnNonNullList_whenSupportedParamsPresent() {
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("page", "1"));
        expected.add(new HttpParameter("count", "10"));
        
        paging.setPage(1);
        paging.setCount(10);
        
        assertEquals(expected, paging.asPostParameterList(Paging.SMCP));
    }
    
    // Test cases for 'asPostParameterArray' method
    
    @Test
    public void asPostParameterArray_shouldReturnEmptyArray_whenNoSupportedParams() {
        HttpParameter[] expected = new HttpParameter[0];
        
        assertEquals(expected, paging.asPostParameterArray(new char[] {}));
    }
    
    @Test
    public void asPostParameterArray_shouldReturnNonNullArray_whenSupportedParamsPresent() {
        HttpParameter[] expected = new HttpParameter[2];
        expected[0] = new HttpParameter("page", "1");
        expected[1] = new HttpParameter("count", "10");
        
        paging.setPage(1);
        paging.setCount(10);
        
        assertArrayEquals(expected, paging.asPostParameterArray(Paging.SMCP));
    }
    
    // Test cases for 'getPage' method
    
    @Test
    public void getPage_shouldReturnDefault_whenNotSet() {
        int expected = -1;
        
        assertEquals(expected, paging.getPage());
    }
    
    @Test
    public void getPage_shouldReturnSetValue_whenSet() {
        int expected = 5;
        
        paging.setPage(5);
        
        assertEquals(expected, paging.getPage());
    }
    
    // Test cases for 'setPage' method
    
    @Test
    public void setPage_shouldSetPage_whenPositiveIntegerPassed() {
        int expected = 10;
        
        paging.setPage(10);
        
        assertEquals(expected, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setPage_shouldThrowException_whenZeroPassed() {
        paging.setPage(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setPage_shouldThrowException_whenNegativeIntegerPassed() {
        paging.setPage(-5);
    }
    
    // Test cases for 'getCount' method
    
    @Test
    public void getCount_shouldReturnDefault_whenNotSet() {
        int expected = -1;
        
        assertEquals(expected, paging.getCount());
    }
    
    @Test
    public void getCount_shouldReturnSetValue_whenSet() {
        int expected = 100;
        
        paging.setCount(100);
        
        assertEquals(expected, paging.getCount());
    }
    
    // Test cases for 'setCount' method
    
    @Test
    public void setCount_shouldSetCount_whenPositiveIntegerPassed() {
        int expected = 50;
        
        paging.setCount(50);
        
        assertEquals(expected, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setCount_shouldThrowException_whenZeroPassed() {
        paging.setCount(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setCount_shouldThrowException_whenNegativeIntegerPassed() {
        paging.setCount(-10);
    }
    
    // Test cases for 'getSinceId' method
    
    @Test
    public void getSinceId_shouldReturnDefault_whenNotSet() {
        long expected = -1L;
        
        assertEquals(expected, paging.getSinceId());
    }
    
    @Test
    public void getSinceId_shouldReturnSetValue_whenSet() {
        long expected = 123456L;
        
        paging.setSinceId(123456L);
        
        assertEquals(expected, paging.getSinceId());
    }
    
    // Test cases for 'setSinceId' method
    
    @Test
    public void setSinceId_shouldSetSinceId_whenPositiveIntegerPassed() {
        long expected = 789654321L;
        
        paging.setSinceId(789654321L);
        
        assertEquals(expected, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setSinceId_shouldThrowException_whenZeroPassed() {
        paging.setSinceId(0L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setSinceId_shouldThrowException_whenNegativeIntegerPassed() {
        paging.setSinceId(-987654321L);
    }
    
    // Test cases for 'getMaxId' method
    
    @Test
    public void getMaxId_shouldReturnDefault_whenNotSet() {
        long expected = -1L;
        
        assertEquals(expected, paging.getMaxId());
    }
    
    @Test
    public void getMaxId_shouldReturnSetValue_whenSet() {
        long expected = 987654321L;
        
        paging.setMaxId(987654321L);
        
        assertEquals(expected, paging.getMaxId());
    }
    
    // Test cases for 'setMaxId' method
    
    @Test
    public void setMaxId_shouldSetMaxId_whenPositiveIntegerPassed() {
        long expected = 654321L;
        
        paging.setMaxId(654321L);
        
        assertEquals(expected, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setMaxId_shouldThrowException_whenZeroPassed() {
        paging.setMaxId(0L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setMaxId_shouldThrowException_whenNegativeIntegerPassed() {
        paging.setMaxId(-123456L);
    }
    
    // Test cases for 'equals' method
    
    @Test
    public void equals_shouldReturnTrue_whenSameObjectPassed() {
        assertTrue(paging.equals(paging));
    }
    
    @Test
    public void equals_shouldReturnTrue_whenSameValuesPassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenDifferentClassPassed() {
        assertFalse(paging.equals(new Object()));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenDifferentPagePassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(2, 10, 1000L, 2000L);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenDifferentCountPassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 1000L, 2000L);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenDifferentSinceIdPassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 500L, 2000L);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void equals_shouldReturnFalse_whenDifferentMaxIdPassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 3000L);
        
        assertFalse(paging1.equals(paging2));
    }
    
    // Test cases for 'hashCode' method
    
    @Test
    public void hashCode_shouldReturnSameValue_whenSameValuesPassed() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    // Test cases for 'toString' method
    
    @Test
    public void toString_shouldReturnCorrectStringRepresentation() {
        String expected = "Paging{page=1, count=10, sinceId=1000, maxId=2000}";
        
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1000L);
        paging.setMaxId(2000L);
        
        assertEquals(expected, paging.toString());
    }
}


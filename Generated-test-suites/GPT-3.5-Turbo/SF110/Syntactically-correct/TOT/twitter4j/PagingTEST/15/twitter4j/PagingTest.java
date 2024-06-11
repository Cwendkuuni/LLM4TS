import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    // test cases for asPostParameterList() method
    @Test
    public void testAsPostParameterList_withValidPaging_returnsListWithParameters() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();
        expectedParams.add(new HttpParameter("since_id", "0"));
        expectedParams.add(new HttpParameter("max_id", "0"));
        expectedParams.add(new HttpParameter("count", "10"));
        expectedParams.add(new HttpParameter("page", "1"));

        List<HttpParameter> actualParams = paging.asPostParameterList();

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsPostParameterList_withInvalidPaging_returnsEmptyList() {
        Paging paging = new Paging();
        List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();

        List<HttpParameter> actualParams = paging.asPostParameterList();

        assertEquals(expectedParams, actualParams);
    }

    // test cases for asPostParameterArray() method
    @Test
    public void testAsPostParameterArray_withValidPaging_returnsArrayWithParameters() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] expectedParams = {new HttpParameter("since_id", "0"), new HttpParameter("max_id", "0"),
                new HttpParameter("count", "10"), new HttpParameter("page", "1")};

        HttpParameter[] actualParams = paging.asPostParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsPostParameterArray_withEmptyPaging_returnsEmptyArray() {
        Paging paging = new Paging();
        HttpParameter[] expectedParams = {};

        HttpParameter[] actualParams = paging.asPostParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    // test cases for getCount() method
    @Test
    public void testGetCount_withValidCount_returnsCount() {
        Paging paging = new Paging(1, 10);

        int count = paging.getCount();

        assertEquals(10, count);
    }

    @Test
    public void testGetCount_withNegativeCount_throwsIllegalArgumentException() {
        Paging paging = new Paging(1, -5);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.getCount();
        });
    }

    // test cases for setCount() method
    @Test
    public void testSetCount_withPositiveCount_setsCount() {
        Paging paging = new Paging();

        paging.setCount(20);

        assertEquals(20, paging.getCount());
    }

    @Test
    public void testSetCount_withNegativeCount_throwsIllegalArgumentException() {
        Paging paging = new Paging();

        assertThrows(IllegalArgumentException.class, () -> {
            paging.setCount(-5);
        });
    }

    // test cases for getSinceId() method
    @Test
    public void testGetSinceId_withValidSinceId_returnsSinceId() {
        Paging paging = new Paging(1, 10);
        paging.setSinceId(12345L);

        long sinceId = paging.getSinceId();

        assertEquals(12345L, sinceId);
    }

    @Test
    public void testGetSinceId_withNegativeSinceId_throwsIllegalArgumentException() {
        Paging paging = new Paging(1, 10);
        paging.setSinceId(-12345L);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.getSinceId();
        });
    }

    // test cases for setSinceId() method
    @Test
    public void testSetSinceId_withPositiveSinceId_setsSinceId() {
        Paging paging = new Paging(1, 10);

        paging.setSinceId(12345L);

        assertEquals(12345L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId_withNegativeSinceId_throwsIllegalArgumentException() {
        Paging paging = new Paging(1, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.setSinceId(-12345L);
        });
    }

    // test cases for getMaxId() method
    @Test
    public void testGetMaxId_withValidMaxId_returnsMaxId() {
        Paging paging = new Paging(1, 10);
        paging.setMaxId(67890L);

        long maxId = paging.getMaxId();

        assertEquals(67890L, maxId);
    }

    @Test
    public void testGetMaxId_withNegativeMaxId_throwsIllegalArgumentException() {
        Paging paging = new Paging(1, 10);
        paging.setMaxId(-67890L);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.getMaxId();
        });
    }

    // test cases for setMaxId() method
    @Test
    public void testSetMaxId_withPositiveMaxId_setsMaxId() {
        Paging paging = new Paging(1, 10);

        paging.setMaxId(67890L);

        assertEquals(67890L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId_withNegativeMaxId_throwsIllegalArgumentException() {
        Paging paging = new Paging(1, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.setMaxId(-67890L);
        });
    }

    // test cases for equals() method
    @Test
    public void testEquals_withSameObject_returnsTrue() {
        Paging paging = new Paging(1, 10);

        boolean equals = paging.equals(paging);

        assertTrue(equals);
    }

    @Test
    public void testEquals_withEqualObjects_returnsTrue() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 10);

        boolean equals = paging1.equals(paging2);

        assertTrue(equals);
    }

    @Test
    public void testEquals_withDifferentObjects_returnsFalse() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(2, 5);

        boolean equals = paging1.equals(paging2);

        assertFalse(equals);
    }

    // test cases for hashCode() method
    @Test
    public void testHashCode_withEqualObjects_returnsEqualHashCodes() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 10);

        int hashCode1 = paging1.hashCode();
        int hashCode2 = paging2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    // test cases for toString() method
    @Test
    public void testToString_withValidPaging_returnsStringRepresentation() {
        Paging paging = new Paging(1, 10);
        String expectedString = "Paging{page=1, count=10, sinceId=0, maxId=0}";

        String actualString = paging.toString();

        assertEquals(expectedString, actualString);
    }

    // additional test cases for edge and error scenarios can be added

}
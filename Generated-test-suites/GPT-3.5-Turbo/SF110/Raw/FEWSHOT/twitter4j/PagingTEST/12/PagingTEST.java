import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList());

        paging.setCount(10);
        assertEquals(1, paging.asPostParameterList().size());
        assertEquals("count", paging.asPostParameterList().get(0).getName());
        assertEquals("10", paging.asPostParameterList().get(0).getValue());

        paging.setSinceId(1234);
        assertEquals(2, paging.asPostParameterList().size());
        assertEquals("since_id", paging.asPostParameterList().get(1).getName());
        assertEquals("1234", paging.asPostParameterList().get(1).getValue());

        paging.setMaxId(5678);
        assertEquals(3, paging.asPostParameterList().size());
        assertEquals("max_id", paging.asPostParameterList().get(2).getName());
        assertEquals("5678", paging.asPostParameterList().get(2).getValue());

        paging.setPage(2);
        assertEquals(4, paging.asPostParameterList().size());
        assertEquals("page", paging.asPostParameterList().get(3).getName());
        assertEquals("2", paging.asPostParameterList().get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray());

        paging.setCount(10);
        assertEquals(1, paging.asPostParameterArray().length);
        assertEquals("count", paging.asPostParameterArray()[0].getName());
        assertEquals("10", paging.asPostParameterArray()[0].getValue());

        paging.setSinceId(1234);
        assertEquals(2, paging.asPostParameterArray().length);
        assertEquals("since_id", paging.asPostParameterArray()[1].getName());
        assertEquals("1234", paging.asPostParameterArray()[1].getValue());

        paging.setMaxId(5678);
        assertEquals(3, paging.asPostParameterArray().length);
        assertEquals("max_id", paging.asPostParameterArray()[2].getName());
        assertEquals("5678", paging.asPostParameterArray()[2].getValue());

        paging.setPage(2);
        assertEquals(4, paging.asPostParameterArray().length);
        assertEquals("page", paging.asPostParameterArray()[3].getName());
        assertEquals("2", paging.asPostParameterArray()[3].getValue());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c'};
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList(supportedParams));

        paging.setCount(10);
        assertEquals(1, paging.asPostParameterList(supportedParams).size());
        assertEquals("count", paging.asPostParameterList(supportedParams).get(0).getName());
        assertEquals("10", paging.asPostParameterList(supportedParams).get(0).getValue());

        paging.setSinceId(1234);
        assertEquals(2, paging.asPostParameterList(supportedParams).size());
        assertEquals("since_id", paging.asPostParameterList(supportedParams).get(1).getName());
        assertEquals("1234", paging.asPostParameterList(supportedParams).get(1).getValue());

        assertNull(paging.getMaxId());

        paging.setPage(2);
        assertEquals(3, paging.asPostParameterList(supportedParams).size());
        assertEquals("page", paging.asPostParameterList(supportedParams).get(2).getName());
        assertEquals("2", paging.asPostParameterList(supportedParams).get(2).getValue());
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c'};
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray(supportedParams));

        paging.setCount(10);
        assertEquals(1, paging.asPostParameterArray(supportedParams).length);
        assertEquals("count", paging.asPostParameterArray(supportedParams)[0].getName());
        assertEquals("10", paging.asPostParameterArray(supportedParams)[0].getValue());

        paging.setSinceId(1234);
        assertEquals(2, paging.asPostParameterArray(supportedParams).length);
        assertEquals("since_id", paging.asPostParameterArray(supportedParams)[1].getName());
        assertEquals("1234", paging.asPostParameterArray(supportedParams)[1].getValue());

        assertNull(paging.getMaxId());

        paging.setPage(2);
        assertEquals(3, paging.asPostParameterArray(supportedParams).length);
        assertEquals("page", paging.asPostParameterArray(supportedParams)[2].getName());
        assertEquals("2", paging.asPostParameterArray(supportedParams)[2].getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNotSupported() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm'};
        paging.addPostParameter(supportedParams, 'c', new ArrayList<>(), "count", 10);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234, 5678);
        Paging paging2 = new Paging(1, 10, 1234, 5678);
        Paging paging3 = new Paging(2, 10, 1234, 5678);

        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        assertEquals(paging1, paging1);
        assertEquals(paging1.hashCode(), paging1.hashCode());

        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertEquals("Paging{page=1, count=10, sinceId=1234, maxId=5678}", paging.toString());
    }
}
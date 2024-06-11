import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuffWrapAround() {
        SimpleCharStream.bufsize = 10;
        SimpleCharStream.buffer = new char[10];
        SimpleCharStream.tokenBegin = 3;
        SimpleCharStream.bufpos = 7;
        SimpleCharStream.maxNextCharInd = 7;
        SimpleCharStream.bufline = new int[10];
        SimpleCharStream.bufcolumn = new int[10];

        SimpleCharStream.ExpandBuff(true);

        assertEquals(17, SimpleCharStream.bufsize);
        assertEquals(0, SimpleCharStream.tokenBegin);
        assertEquals(0, SimpleCharStream.bufpos);
        assertEquals(10, SimpleCharStream.maxNextCharInd);
    }

    @Test
    public void testExpandBuffNoWrapAround() {
        SimpleCharStream.bufsize = 10;
        SimpleCharStream.buffer = new char[10];
        SimpleCharStream.tokenBegin = 3;
        SimpleCharStream.bufpos = 7;
        SimpleCharStream.maxNextCharInd = 7;
        SimpleCharStream.bufline = new int[10];
        SimpleCharStream.bufcolumn = new int[10];

        SimpleCharStream.ExpandBuff(false);

        assertEquals(14, SimpleCharStream.bufsize);
        assertEquals(3, SimpleCharStream.tokenBegin);
        assertEquals(6, SimpleCharStream.bufpos);
        assertEquals(6, SimpleCharStream.maxNextCharInd);
    }

    @Test
    public void testFillBuffAvailableEqualsBufsize() throws IOException {
        SimpleCharStream.available = 10;
        SimpleCharStream.bufsize = 10;
        SimpleCharStream.tokenBegin = 3;
        SimpleCharStream.bufpos = 7;
        SimpleCharStream.maxNextCharInd = 7;
        SimpleCharStream.buffer = new char[10];
        SimpleCharStream.bufline = new int[10];
        SimpleCharStream.bufcolumn = new int[10];

        SimpleCharStream.FillBuff();

        assertEquals(0, SimpleCharStream.bufpos);
        assertEquals(0, SimpleCharStream.available);
    }

    @Test
    public void testFillBuffAvailableGreaterThanTokenBegin() throws IOException {
        SimpleCharStream.available = 15;
        SimpleCharStream.bufsize = 10;
        SimpleCharStream.tokenBegin = 3;
        SimpleCharStream.bufpos = 7;
        SimpleCharStream.maxNextCharInd = 7;
        SimpleCharStream.buffer = new char[10];
        SimpleCharStream.bufline = new int[10];
        SimpleCharStream.bufcolumn = new int[10];

        SimpleCharStream.FillBuff();

        assertEquals(15, SimpleCharStream.available);
    }

    @Test
    public void testFillBuffTokenBeginMinuAvailableLessThan2048() throws IOException {
        SimpleCharStream.available = 10;
        SimpleCharStream.bufsize = 10;
        SimpleCharStream.tokenBegin = 3;
        SimpleCharStream.bufpos = 9;
        SimpleCharStream.maxNextCharInd = 9;
        SimpleCharStream.buffer = new char[10];
        SimpleCharStream.bufline = new int[10];
        SimpleCharStream.bufcolumn = new int[10];

        SimpleCharStream.FillBuff();

        assertEquals(10, SimpleCharStream.available);
    }
}
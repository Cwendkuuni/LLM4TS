import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000L);
        assertEquals(1000L, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000L);
        assertEquals(1000L, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000L);
        x7875.setGID(1000L);
        assertEquals(9, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(2000L);
        assertEquals(2000L, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(3000L);
        assertEquals(3000L, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix expected = new X7875_NewUnix();
        expected.setUID(4000L);
        expected.setGID(5000L);

        X7875_NewUnix actual = new X7875_NewUnix();
        actual.parseFromLocalFileData(new byte[]{1, 2, 4, 1, 20, 40, 3, 5}, 2, 6);

        assertEquals(expected.getUID(), actual.getUID());
        assertEquals(expected.getGID(), actual.getGID());
    }
}
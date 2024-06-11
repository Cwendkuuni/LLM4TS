import org.junit.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream("zip", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveOutputStream("tar", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullInputStreamThreeArgs() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullInputStreamMarkNotSupported() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
  }

  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
  }

  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_AutoDetect_NoArchiverFound() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Zip() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{80, 75, 3, 4}));
    assertTrue(in instanceof ZipArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Jar() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{80, 75, 3, 4, 20, 0}));
    assertTrue(in instanceof JarArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Ar() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{33, 60, 97, 114}));
    assertTrue(in instanceof ArArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Cpio() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{070, 073, 012}));
    assertTrue(in instanceof CpioArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Dump() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{66, 68, 0, 0}));
    assertTrue(in instanceof DumpArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect_Tar() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{48, 0, 0, 0, 0, 0, 0, 0}));
    assertTrue(in instanceof TarArchiveInputStream);
  }

  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_AutoDetect_NotTar_CatchException() throws ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(new ByteArrayInputStream(
        new byte[]{48, 0, 0, 0, 0, 0, 0, 0, 0}));
  }
  
}
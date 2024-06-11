import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

  @Test
  public void createArchiveInputStream_withValidArchiverNameAndInputStream_shouldReturnArchiveInputStream() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        return 0;
      }
    };
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    assertNotNull(archiveInputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createArchiveInputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        return 0;
      }
    };
    factory.createArchiveInputStream(null, inputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
  }

  @Test
  public void createArchiveOutputStream_withValidArchiverNameAndOutputStream_shouldReturnArchiveOutputStream() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream outputStream = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
      }
    };
    ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
    assertNotNull(archiveOutputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createArchiveOutputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream outputStream = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
      }
    };
    factory.createArchiveOutputStream(null, outputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
  }

  @Test
  public void createArchiveInputStream_withInputStream_shouldReturnArchiveInputStream() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        return 0;
      }
    };
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
    assertNotNull(archiveInputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws IOException, ArchiveException {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    factory.createArchiveInputStream(null);
  }

}
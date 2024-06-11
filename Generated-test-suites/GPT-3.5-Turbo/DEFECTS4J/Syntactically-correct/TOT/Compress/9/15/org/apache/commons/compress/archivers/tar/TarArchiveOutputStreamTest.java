import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

  private TarArchiveOutputStream tao;
  private ByteArrayOutputStream baos;
  
  @Before
  public void setUp() {
    baos = new ByteArrayOutputStream();
    tao = new TarArchiveOutputStream(baos);
  }
  
  @After
  public void tearDown() throws IOException {
    if (tao != null) {
      tao.close();
    }
    if (baos != null) {
      baos.close();
    }
  }
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.closeArchiveEntry();
    
    assertEquals(512, baos.size());
  }
  
  @Test(expected = IOException.class)
  public void testPutArchiveEntryFinished() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.finish();
    tao.putArchiveEntry(entry);
  }
  
  @Test(expected = IOException.class)
  public void testPutArchiveEntryLongFileName() throws IOException {
    // Constructing a TarArchiveEntry with name length > TarConstants.NAMELEN
    StringBuffer nameBuf = new StringBuffer();
    while (nameBuf.length() <= TarConstants.NAMELEN) {
      nameBuf.append("a");
    }
    TarArchiveEntry entry = new TarArchiveEntry(nameBuf.toString());
    
    tao.putArchiveEntry(entry);
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.closeArchiveEntry();
    
    assertEquals(512, baos.size());
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryFinished() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.finish();
    tao.closeArchiveEntry();
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
    tao.closeArchiveEntry();
  }
  
  @Test(expected = IOException.class)
  public void testWriteFinished() throws IOException {
    tao.finish();
    tao.write(new byte[1024]);
  }
  
  @Test(expected = IOException.class)
  public void testWriteExceedSize() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.write(new byte[20]);
  }
  
  @Test
  public void testWrite() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.write(new byte[10]);
    
    assertEquals(512, baos.size());
  }
  
  @Test
  public void testFinish() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.finish();
    
    assertEquals(1024, baos.size());
  }
  
  @Test(expected = IOException.class)
  public void testFinishAlreadyFinished() throws IOException {
    tao.finish();
    tao.finish();
  }
  
  @Test(expected = IOException.class)
  public void testFinishContainsUnclosedEntries() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.finish();
    tao.finish();
  }
  
  @Test
  public void testClose() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    entry.setSize(10);
    
    tao.putArchiveEntry(entry);
    tao.close();
    
    assertEquals(1024, baos.size());
  }
  
  @Test(expected = IOException.class)
  public void testCloseAlreadyClosed() throws IOException {
    tao.close();
    tao.close();
  }
  
  @Test
  public void testGetRecordSize() {
    assertEquals(512, tao.getRecordSize());
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    File inputFile = new File("input.txt");
    inputFile.createNewFile();
    
    String entryName = "test.txt";
    TarArchiveEntry entry = (TarArchiveEntry) tao.createArchiveEntry(inputFile, entryName);
    assertEquals(inputFile.getAbsolutePath(), entry.getFile().getAbsolutePath());
    assertEquals(entryName, entry.getName());
    
    inputFile.delete();
  }
  
  @Test(expected = IOException.class)
  public void testCreateArchiveEntryFinished() throws IOException {
    File inputFile = new File("input.txt");
    inputFile.createNewFile();
    
    String entryName = "test.txt";
    
    tao.finish();
    tao.createArchiveEntry(inputFile, entryName);
    
    inputFile.delete();
  }
}
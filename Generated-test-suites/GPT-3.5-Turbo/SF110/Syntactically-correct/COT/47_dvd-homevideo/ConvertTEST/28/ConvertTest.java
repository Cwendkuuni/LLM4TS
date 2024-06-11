
import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {
  
  @Test
  public void testInit() {
    Convert convert = new Convert("format", "aspectRatio", new GUI());
    
    boolean actual = convert.init();
    
    assertFalse(actual);
  }
  
  @Test
  public void testRun() {
    Convert convert = new Convert("format", "aspectRatio", new GUI());
    
    convert.run();
    
    assertEquals("transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30", convert.m_transcode);
  }
  
  @Test
  public void testTranscode() {
    Convert convert = new Convert("format", "aspectRatio", new GUI());
    convert.video_files = new String[] {"file1.dv", "file2.dv"};
    convert.thread_track = 1;
    
    convert.Transcode();
    
    assertEquals("transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30", convert.m_transcode);
  }
  
  @Test
  public void testMatrix() {
    Convert convert = new Convert("format", "aspectRatio", new GUI());
    
    convert.matrix();
    
    // assertion for file creation
    // ...
  }
}

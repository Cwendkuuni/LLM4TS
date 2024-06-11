import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    
    private Convert convert;
    private String[] video_files;
    
    @Before
    public void setUp() {
        GUI gui = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", gui);
        video_files = new String[]{"video1.dv", "video2.dv"};
    }
    
    @Test
    public void testInit() {
        convert.video_files = video_files;
        assertTrue(convert.init());
    }
    
    @Test
    public void testTranscode() {
        convert.video_files = video_files;
        convert.Transcode();
        String expectedTranscode = "transcode -i dv/" + video_files[0] + " -m test1.ac3 -o test1 -w 8500 -x dv,dv -F '8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2' -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof dvd-ntsc --export_fps 29.97 --export_asr 16:9 -j 0,8,0,8 --print_status 30";
        String expectedMplex = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
        assertEquals(expectedTranscode, convert.m_transcode);
        assertEquals(expectedMplex, convert.mplex);
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        // Verify that matrix.txt file is created
        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(matrixFile.exists());
    }
}
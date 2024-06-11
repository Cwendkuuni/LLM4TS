package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        frame.theta = mock(JTextField.class);
        frame.lat = mock(JTextField.class);
        frame.lon = mock(JTextField.class);
        frame.display = mock(HuntDisplay.class);
        frame.points.add(mock(TrigPoint.class));
        frame.fox.add(mock(TrigPoint.class));

        frame.addPoint();

        verify(frame.theta).getText();
        verify(frame.lat).getText();
        verify(frame.lon).getText();
        verify(frame.lat).getText();
        verify(frame.display).addPoint(any(TrigPoint.class));
        verify(frame.display).addPoint(any(TrigPoint.class));
        assertEquals(2, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        File file = mock(File.class);
        FileInputStream inFile = mock(FileInputStream.class);
        BufferedReader in = mock(BufferedReader.class);
        when(file.exists()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("./landmarks.cfg");
        try {
            whenNew(File.class).withArguments("./landmarks.cfg").thenReturn(file);
            whenNew(FileInputStream.class).withArguments(file).thenReturn(inFile);
            whenNew(BufferedReader.class).withArguments(new InputStreamReader(inFile)).thenReturn(in);

            when(in.readLine()).thenReturn("NUM=2");
            when(in.readLine()).thenReturn("FILEfile1.gif");
            when(in.readLine()).thenReturn("FILEfile2.gif");
            when(in.readLine()).thenReturn("LAT10.0");
            when(in.readLine()).thenReturn("LAT20.0");
            when(in.readLine()).thenReturn("LON50.0");
            when(in.readLine()).thenReturn("LON60.0");
            whenNew(ImageIcon.class).withArguments("file1.gif").thenReturn(mock(ImageIcon.class));
            whenNew(ImageIcon.class).withArguments("file2.gif").thenReturn(mock(ImageIcon.class));

            frame.display = mock(HuntDisplay.class);
            frame.loadLandmarks();

            verify(new File("./landmarks.cfg")).exists();
            verify(frame.display).addLandMark(any(Landmark.class));
            verify(frame.display).addLandMark(any(Landmark.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
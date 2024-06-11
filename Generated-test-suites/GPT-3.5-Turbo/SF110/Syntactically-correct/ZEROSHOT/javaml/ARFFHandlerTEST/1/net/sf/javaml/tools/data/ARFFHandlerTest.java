
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {
    
    private File testFile;
    private LineIterator lineIterator;
    private Dataset dataset;
    
    @Before
    public void setUp() {
        testFile = mock(File.class);
        lineIterator = mock(LineIterator.class);
        dataset = mock(Dataset.class);
    }
    
    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenReturn("1,2,3", "@data");
        
        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenReturn("1,2,3");
        
        when(testFile.exists()).thenReturn(true);
        when(testFile.isFile()).thenReturn(true);
        
        whenNew(LineIterator.class).withArguments(testFile).thenReturn(lineIterator);
        
        whenNew(DefaultDataset.class).thenReturn(dataset);
        
        ARFFHandler.loadARFF(testFile);
        
        verify(testFile).exists();
        verify(testFile).isFile();
        verifyNew(LineIterator.class).withArguments(testFile);
        verify(lineIterator, times(4)).hasNext();
        verify(lineIterator, times(2)).next();
        
        verify(dataset).add(Mockito.<Instance>any());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        int classIndex = 2;
        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenReturn("1,2,3", "@data");
        
        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenReturn("1,2,3");
        
        when(testFile.exists()).thenReturn(true);
        when(testFile.isFile()).thenReturn(true);
        
        whenNew(LineIterator.class).withArguments(testFile).thenReturn(lineIterator);
        
        whenNew(DefaultDataset.class).thenReturn(dataset);
        
        ARFFHandler.loadARFF(testFile, classIndex);
        
        verify(testFile).exists();
        verify(testFile).isFile();
        verifyNew(LineIterator.class).withArguments(testFile);
        verify(lineIterator, times(4)).hasNext();
        verify(lineIterator, times(2)).next();
        
        verify(dataset).add(Mockito.<Instance>any());
    }
}

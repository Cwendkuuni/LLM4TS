
package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.swing.filechooser.FileFilter;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

public class AbrirTest {
    
    @Mock
    ProcessarEntidades form;
    
    @Mock
    JFileChooser escolhedor;
    
    @Mock
    FileFilter filtro;
    
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructors(){
        Abrir abrir1 = new Abrir(form, "arquivo.jaw.xml");
        verify(form).setNomeArquivoSalvo("arquivo.jaw.xml");
        verify(form).setEntidades(any());
        
        Abrir abrir2 = new Abrir(form, 10);
        verify(form, times(2)).setNomeArquivoSalvo(anyString());
        verify(form, times(2)).setEntidades(any());
        verify(escolhedor).setCurrentDirectory(any());
        verify(escolhedor).showOpenDialog(null);
        
        Abrir abrir3 = new Abrir(form, 20);
        verify(form, times(2)).setNomeArquivoSalvo(anyString());
        verify(form, times(2)).setEntidades(any());
        verify(escolhedor).setCurrentDirectory(any());
        verify(escolhedor).showOpenDialog(null);
        
    }
    
    @Test
    public void testAbrirSerializado()
    {
        Abrir abrir = new Abrir(form, "");
        
        abrir.abrirSerializado("");
        verify(form).setEntidades(any());
        
        abrir.abrirSerializado("arquivo.jaw");
        verify(form).setNomeArquivoSalvo("arquivo.jaw");
        verify(form).setEntidades(any());
        
        abrir.abrirSerializado("arquivo.txt");
        verifyNoInteractions(form);
        verifyNoMoreInteractions(form);
    }
    
    @Test
    public void testAbrirXML()
    {
        Abrir abrir = new Abrir(form, "");
        
        abrir.abrirXML("");
        verify(form).setEntidades(any());
        
        abrir.abrirXML("arquivo.jaw.xml");
        verify(form).setNomeArquivoSalvo("arquivo.jaw.xml");
        verify(form).setEntidades(any());
        
        abrir.abrirSerializado("arquivo.txt");
        verifyNoInteractions(form);
        verifyNoMoreInteractions(form);
    }
    
    @Test
    public void testFiltro2Accept()
    {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        
        // Mock File directory
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        
        // Mock File
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("arquivo.jaw.xml");
        
        assertTrue(filtro2.accept(directory));
        assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2GetDescription()
    {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept()
    {
        Abrir.Filtro filtro = new Abrir.Filtro();
        
        // Mock File directory
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        
        // Mock File
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("arquivo.jaw");
        
        assertTrue(filtro.accept(directory));
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroGetDescription()
    {
        Abrir.Filtro filtro = new Abrir.Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
    
}

import static org.junit.Assert.*;
import org.junit.Test;
import jaw.gui.*;
import org.mockito.*;

public class AbrirTest {
    
    @Mock
    ProcessarEntidades form;
    
    @Test
    public void testAbrirConstructorWithArquivoEndingWithXML() {
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testAbrirConstructorWithArquivoNotEndingWithXML() {
        String arquivo = "test.jaw";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testAbrirConstructorWithTipoAbrirTen() {
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        ArgumentCaptor<FileFilter> argumentCaptor = ArgumentCaptor.forClass(FileFilter.class);
        Mockito.verify(escolhedor).setFileFilter(argumentCaptor.capture());
        Filtro2 filtro = (Filtro2) argumentCaptor.getValue();
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
    @Test
    public void testAbrirConstructorWithTipoAbrirTwenty() {
        int tipoAbrir = 20;
        Abrir abrir = new Abrir(form, tipoAbrir);
        ArgumentCaptor<FileFilter> argumentCaptor = ArgumentCaptor.forClass(FileFilter.class);
        Mockito.verify(escolhedor).setFileFilter(argumentCaptor.capture());
        Filtro filtro = (Filtro) argumentCaptor.getValue();
        assertEquals("JAW files", filtro.getDescription());
    }

    @Test
    public void testAbrirSerializadoWithArquivoEndingWithJaw() {
        String arquivo = "test.jaw";
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, 20);
        abrir.abrirSerializado(arquivo);
        Mockito.verify(form).setNomeArquivoSalvo(arquivo);
        Mockito.verify(form).setEntidades(Mockito.any());
    }

    @Test
    public void testAbrirSerializadoWithArquivoNotEndingWithJaw() {
        String arquivo = "test.file";
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, 20);
        abrir.abrirSerializado(arquivo);
        Mockito.verify(form, Mockito.never()).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form, Mockito.never()).setEntidades(Mockito.any());
        Mockito.verify(JOptionPane).showMessageDialog(null, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0);
    }

    @Test
    public void testAbrirXMLWithArquivoEndingWithJawXML() {
        String arquivo = "test.jaw.xml";
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, 10);
        abrir.abrirXML(arquivo);
        Mockito.verify(form).setNomeArquivoSalvo(arquivo);
        Mockito.verify(form).setEntidades(Mockito.any());
    }

    @Test
    public void testAbrirXMLWithArquivoNotEndingWithJawXML() {
        String arquivo = "test.file";
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, 10);
        abrir.abrirXML(arquivo);
        Mockito.verify(form, Mockito.never()).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form, Mockito.never()).setEntidades(Mockito.any());
        Mockito.verify(JOptionPane).showMessageDialog(null, "Arquivo xml corrompido ou incompatível com aplicação!", "Importação XML", 0);
    }

    @Test
    public void testFiltro2AcceptWithDirectory() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(true);
        Filtro2 filtro = new Filtro2();
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltro2AcceptWithJawXML() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.jaw.xml");
        Filtro2 filtro = new Filtro2();
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltro2AcceptWithOtherExtension() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.file");
        Filtro2 filtro = new Filtro2();
        assertFalse(filtro.accept(file));
    }

    @Test
    public void testFiltro2GetDescription() {
        Filtro2 filtro = new Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltroAcceptWithDirectory() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(true);
        Filtro filtro = new Filtro();
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroAcceptWithJaw() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.jaw");
        Filtro filtro = new Filtro();
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroAcceptWithOtherExtension() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.file");
        Filtro filtro = new Filtro();
        assertFalse(filtro.accept(file));
    }

    @Test
    public void testFiltroGetDescription() {
        Filtro filtro = new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}
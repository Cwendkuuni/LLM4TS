package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import jaw.ProcessarEntidades;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades mockForm;

    @Before
    public void setup() {
        mockForm = mock(ProcessarEntidades.class);
    }

    @Test
    public void testConstructorWithArquivoEndingWithJawXml() {
        Abrir abrir = new Abrir(mockForm, "file.jaw.xml");
        
        verify(mockForm, never()).setNomeArquivoSalvo(anyString());
        verify(mockForm, never()).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testConstructorWithArquivoEndingWithJaw() {
        Abrir abrir = new Abrir(mockForm, "file.jaw");
        
        verify(mockForm).setNomeArquivoSalvo(anyString());
        verify(mockForm).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testConstructorWithTipoAbrir10() {
        Abrir abrir = new Abrir(mockForm, Abrir.ABRIR_XML);
        
        verify(mockForm, never()).setNomeArquivoSalvo(anyString());
        verify(mockForm, never()).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testConstructorWithTipoAbrir20() {
        Abrir abrir = new Abrir(mockForm, Abrir.ABRIR);
        
        verify(mockForm).setNomeArquivoSalvo(anyString());
        verify(mockForm).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testAbrirSerializadoWithArquivoEndingWithJaw() {
        Abrir abrir = new Abrir(mockForm, null);
        abrir.abrirSerializado("file.jaw");
        
        verify(mockForm).setNomeArquivoSalvo(anyString());
        verify(mockForm).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testAbrirSerializadoWithArquivoNotEndingWithJaw() {
        Abrir abrir = new Abrir(mockForm, null);
        abrir.abrirSerializado("file.xml");
        
        verify(mockForm, never()).setNomeArquivoSalvo(anyString());
        verify(mockForm, never()).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testAbrirXMLWithArquivoEndingWithJawXml() {
        Abrir abrir = new Abrir(mockForm, null);
        abrir.abrirXML("file.jaw.xml");
        
        verify(mockForm).setNomeArquivoSalvo(anyString());
        verify(mockForm).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testAbrirXMLWithArquivoNotEndingWithJawXml() {
        Abrir abrir = new Abrir(mockForm, null);
        abrir.abrirXML("file.xml");
        
        verify(mockForm, never()).setNomeArquivoSalvo(anyString());
        verify(mockForm, never()).setEntidades(any());
        verifyNoMoreInteractions(mockForm);
    }

    @Test
    public void testAcceptWithFileEndingWithJawXml() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        boolean result = filtro.accept(new File("file.jaw.xml"));

        assertEquals(true, result);
    }

    @Test
    public void testAcceptWithFileNotEndingWithJawXml() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        boolean result = filtro.accept(new File("file.xml"));

        assertEquals(false, result);
    }

    @Test
    public void testGetDescription() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        String result = filtro.getDescription();

        assertEquals("XML JAW files", result);
    }

    @Test
    public void testAcceptWithFileEndingWithJaw() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        boolean result = filtro.accept(new File("file.jaw"));

        assertEquals(true, result);
    }

    @Test
    public void testAcceptWithFileNotEndingWithJaw() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        boolean result = filtro.accept(new File("file.xml"));

        assertEquals(false, result);
    }

    @Test
    public void testGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        String result = filtro.getDescription();

        assertEquals("JAW files", result);
    }
}
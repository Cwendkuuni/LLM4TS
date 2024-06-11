package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import jaw.ProcessarEntidades;
import javax.swing.filechooser.FileFilter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class AbrirTest {

    @Test
    public void testConstructor1() throws Exception {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        FileFilter filtro = spy(new Abrir.Filtro2());
        File arquivo = mock(File.class);
        Reader leitor = mock(Reader.class);
        SerializacaoXML serializacaoXML = spy(new SerializacaoXML());

        when(arquivo.getAbsolutePath()).thenReturn("path/to/file.jaw.xml");
        when(arquivo.getName()).thenReturn("file.jaw.xml");
        when(arquivo.isDirectory()).thenReturn(false);
        when(arquivo.exists()).thenReturn(true);
        when(arquivo.canRead()).thenReturn(true);

        when(filtro.accept(arquivo)).thenReturn(true);

        when(form.salvarTexto(anyString(), anyString())). thenReturn(true);
        when(form.getContentPane()).thenReturn(spy(new Container()));

        when(serializacaoXML.xml2Entidades(any(Reader.class))).thenReturn(true);

        doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            assertEquals("file.jaw.xml", arguments[0]);
            FileReader reader = (FileReader) arguments[1];
            assertEquals(leitor, reader);
            return null;
        }).when(form).setEntidades(any());
     
        Abrir abrir = new Abrir(form, "path/to/file.jaw.xml");

        verify(filtro).accept(arquivo);
        verify(form).setEntidades(true);
    }

    @Test
    public void testConstructor2() throws Exception {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        FileFilter filtro = spy(new Abrir.Filtro());
        File arquivo = mock(File.class);
        Serializacao serializacao = spy(new Serializacao());

        when(arquivo.getAbsolutePath()).thenReturn("path/to/file.jaw");
        when(arquivo.getName()).thenReturn("file.jaw");
        when(arquivo.isDirectory()).thenReturn(false);
        when(arquivo.exists()).thenReturn(true);
        when(arquivo.canRead()).thenReturn(true);

        when(filtro.accept(arquivo)).thenReturn(true);

        when(form.salvarTexto(anyString(), anyString())). thenReturn(true);
        when(form.getContentPane()).thenReturn(spy(new Container()));

        doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            assertEquals("file.jaw", arguments[0]);
            return null;
        }).when(form).setEntidades(any());

        Abrir abrir = new Abrir(form, 20);

        verify(filtro).accept(arquivo);
        verify(form).setEntidades(true);
    }

    @Test
    public void testConstructor3() throws Exception {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        FileFilter filtro = spy(new Abrir.Filtro());
        File arquivo = mock(File.class);

        when(filtro.accept(arquivo)).thenReturn(false);

        when(form.salvarTexto(anyString(), anyString())). thenReturn(true);
        when(form.getContentPane()).thenReturn(spy(new Container()));

        Abrir abrir = new Abrir(form, 20);

        verify(filtro).accept(arquivo);
        verify(form, never()).setEntidades(true);
    }
}
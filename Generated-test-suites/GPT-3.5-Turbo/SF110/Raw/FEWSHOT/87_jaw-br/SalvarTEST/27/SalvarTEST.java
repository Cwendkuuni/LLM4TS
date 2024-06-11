package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import org.junit.*;

public class SalvarTest {
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        verify(form).getEntidades();
        verifyNoMoreInteractions(form);
    }
    
    @Test
    public void testSalvarConstructor_exception() throws Exception {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 10;
        doThrow(new Exception()).when(form).getEntidades();
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        verify(form).getEntidades();
        verify(Main.janelaPrincipal).log(anyString());
        verifyNoMoreInteractions(form, Main.janelaPrincipal);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs10() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 10;
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        when(form.getName()).thenReturn("");
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro2.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getName();
        verifyNoMoreInteractions(form, escolhedor);
        assertNull(salvar.vela);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs20Or30() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 20;
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        when(form.getName()).thenReturn("");
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getName();
        verifyNoMoreInteractions(form, escolhedor);
        assertNull(salvar.vela);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs20AndNomeArquivoSalvoIsNovoProjeto() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 20;
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("file");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getNomeArquivoSalvo();
        verify(escolhedor).getSelectedFile();
        verifyZeroInteractions(form);
        assertEquals("file", salvar.arquivo);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs20AndNomeArquivoSalvoIsNotNovoProjeto() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 20;
        when(form.getNomeArquivoSalvo()).thenReturn("file");
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verifyZeroInteractions(escolhedor);
        verify(form).getNomeArquivoSalvo();
        verifyZeroInteractions(escolhedor);
        assertEquals("file", salvar.arquivo);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs30() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 30;
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        when(form.getName()).thenReturn("");
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getName();
        verifyNoMoreInteractions(form, escolhedor);
        assertNull(salvar.vela);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs30Or20AndTipoSalvarIsNotNovoProjeto() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 30;
        when(form.getNomeArquivoSalvo()).thenReturn("file");
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verifyZeroInteractions(escolhedor);
        verify(form).getNomeArquivoSalvo();
        verifyZeroInteractions(escolhedor);
        assertEquals("file", salvar.arquivo);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs30AndTipoSalvarIsNovoProjeto() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 30;
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("file");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getNomeArquivoSalvo();
        verify(escolhedor).getSelectedFile();
        verifyZeroInteractions(form);
        assertEquals("file", salvar.arquivo);
    }
    
    @Test
    public void testSalvarConstructor_tipoSalvarIs10AndArquivoDoesNotEndWith_jaw_xml() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 10;
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("file");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        SerializacaoXML serializaXML = mock(SerializacaoXML.class);
        whenNew(SerializacaoXML.class).withNoArguments().thenReturn(serializaXML);
        when(serializaXML.entidades2XML(any())).thenReturn("xml");
        FileWriter escrevedor = mock(FileWriter.class);
        whenNew(FileWriter.class).withParameterTypes(String.class).withArguments("file.jaw.xml").thenReturn(escrevedor);
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        verify(escolhedor).setFileFilter(any(Filtro2.class));
        verify(escolhedor).showSaveDialog(form);
        verify(form).getNomeArquivoSalvo();
        verify(escolhedor).getSelectedFile();
        verifyNoMoreInteractions(form, escolhedor);
        verifyNew(SerializacaoXML.class).withNoArguments();
        verify(serializaXML).entidades2XML(any());
        verify(escrevedor).write("xml");
        verify(escrevedor).flush();
        verify(escrevedor).close();
        verify(JOptionPane).showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
        verifyNoMoreInteractions(serializaXML, escrevedor, JOptionPane);
        assertEquals("file.jaw.xml", salvar.arquivo);
        assertEquals("xml", salvar.xml);
    }
}
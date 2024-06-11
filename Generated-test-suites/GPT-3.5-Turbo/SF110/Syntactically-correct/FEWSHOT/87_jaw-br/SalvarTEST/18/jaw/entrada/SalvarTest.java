import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SalvarTest {
    @Test
    public void testConstructor() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        verify(form, times(1)).getEntidades();
    }
    
    @Test
    public void testConstructorException() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenThrow(new Exception("Test Exception"));
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        // Verify that the exception is logged
        verify(Main.janelaPrincipal, times(1)).log(anyString());
    }
    
    @Test
    public void testConstructorTipoSalvar10() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        SerializacaoXML serializaXML = mock(SerializacaoXML.class);
        FileWriter escrevedor = mock(FileWriter.class);
        
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        when(escolhedor.getNextSelectionIndex()).thenReturn(0);
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        when(selectedFile.getAbsolutePath()).thenReturn("testFilePath");
        when(serializaXML.entidades2XML(anyList())).thenReturn("testXml");
        
        doNothing().when(escrevedor).write(anyString());
        doNothing().when(escrevedor).flush();
        doNothing().when(escrevedor).close();
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        verify(escolhedor, times(1)).showSaveDialog(form);
        verify(escolhedor, times(1)).setFileFilter(any(Filtro2.class));
        verify(serializaXML, times(1)).entidades2XML(anyList());
        verify(escrevedor, times(1)).write(anyString());
        verify(escrevedor, times(1)).flush();
        verify(escrevedor, times(1)).close();
        verify(JOptionPane, times(1)).showMessageDialog(eq(form), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }
    
    @Test
    public void testConstructorTipoSalvar20() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        Serializacao salvar = mock(Serializacao.class);
        
        when(escolhedor.getNextSelectionIndex()).thenReturn(1);
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        when(selectedFile.getAbsolutePath()).thenReturn("testFilePath");
        when(salvar.salvar(anyList(), anyString())).thenReturn(true);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        
        verify(escolhedor, times(1)).showSaveDialog(form);
        verify(escolhedor, times(1)).setFileFilter(any(Filtro.class));
        verify(salvar, times(1)).salvar(anyList(), anyString());
        verify(JOptionPane, times(1)).showMessageDialog(eq(form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }
    
    @Test
    public void testConstructorTipoSalvar30() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        Serializacao salvar = mock(Serializacao.class);
        
        when(escolhedor.getNextSelectionIndex()).thenReturn(2);
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        when(selectedFile.getAbsolutePath()).thenReturn("testFilePath");
        when(salvar.salvar(anyList(), anyString())).thenReturn(true);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);
        
        verify(escolhedor, times(1)).showSaveDialog(form);
        verify(escolhedor, times(1)).setFileFilter(any(Filtro.class));
        verify(salvar, times(1)).salvar(anyList(), anyString());
        verify(JOptionPane, times(1)).showMessageDialog(eq(form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }
    
    @Test
    public void testConstructorTipoSalvar20NomeArquivoSalvo() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser escolhedor = mock(JFileChooser.class);
        Serializacao salvar = mock(Serializacao.class);
        
        when(form.getNomeArquivoSalvo()).thenReturn("testNome");
        when(escolhedor.getNextSelectionIndex()).thenReturn(1);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        
        verify(escolhedor, times(1)).showSaveDialog(form);
        verify(escolhedor, times(1)).setFileFilter(any(Filtro.class));
        verify(salvar, times(1)).salvar(anyList(), eq("testNome"));
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(JOptionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), anyInt());
    }
    
    @Test
    public void testConstructorTipoSalvar30NomeArquivoSalvo() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser escolhedor = mock(JFileChooser.class);
        Serializacao salvar = mock(Serializacao.class);
        
        when(form.getNomeArquivoSalvo()).thenReturn("testNome");
        when(escolhedor.getNextSelectionIndex()).thenReturn(2);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);
        
        verify(escolhedor, times(1)).showSaveDialog(form);
        verify(escolhedor, times(1)).setFileFilter(any(Filtro.class));
        verify(salvar, times(1)).salvar(anyList(), eq("testNome"));
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(JOptionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), anyInt());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;
import java.util.List;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.io.FileWriter;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Testar os valores após a criação do objeto Salvar
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        
        // Testar se o método getEntidades é chamado corretamente
        assertTrue(form.getEntidadesCalled);
        
        // Testar se o objeto JFileChooser é criado e configurado corretamente
        JFileChooser escolhedor = salvar.escolhedor;
        assertNotNull(escolhedor);
        assertEquals(new File(Main.configuracao.getCaminhoDosProjetos()), escolhedor.getCurrentDirectory());
        
        // Testar se o FileFilter correto é definido de acordo com o tipo de salvar
        if (tipoSalvar == 10) {
            assertTrue(escolhedor.getFileFilter() instanceof Filtro2);
        }
        if (tipoSalvar == 20 || tipoSalvar == 30) {
            assertTrue(escolhedor.getFileFilter() instanceof Filtro);
        }
        
        // Testar se o método showSaveDialog é chamado corretamente
        assertTrue(escolhedor.showSaveDialogCalled);
        assertEquals(form, escolhedor.showSaveDialogArgument);
        
        // Testar se o valor de vela é corretamente atualizado
        assertTrue(salvar.vela);
        
        // Testar a condição if (escolhedor.getSelectedFile() == null && form.getName().equals(""))
        // Observe que o valor de vela é mantido como true
        assertFalse(escolhedor.getSelectedFileCalled);
        
        // Testar a condição if (vela)
        // Observe que o valor de vela é mantido como true
        assertFalse(escolhedor.getSelectedFileCalled);
        
        // Testar a condição if (this.vela)
        // Observe que o valor de arquivo é atualizado corretamente
        assertEquals(escolhedor.getSelectedFileArgument.getAbsolutePath(), salvar.arquivo);
        
        // Testar a condição if (!this.arquivo.endsWith(".jaw") && this.vela)
        // Observe que o valor de arquivo é atualizado corretamente
        saveOriginalArquivo = salvar.arquivo;
        salvar.arquivo = "";
        salvar.vela = true;
        
        assertEquals(saveOriginalArquivo + ".jaw", salvar.arquivo);
        assertFalse(salvar.vela);
        
        // Testar se os métodos de Serializacao são chamados corretamente
        assertEquals(form.getNomeArquivoSalvo(), salvar.arquivo);
        assertEquals(form.getNomeArquivoSalvoCalledTimes, 2);
        
        Serializacao salvarSerializa = salvar.salvar;
        assertNotNull(salvarSerializa);
        
        assertEquals(salvar.entidade, salvarSerializa.entidades);
        
        assertEquals(salvar.arquivo, salvarSerializa.arquivo);
        
        if (salvarSerializa.salvarCalled) {
            JOptionPane.showMessageDialog(form, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
        }
        assertFalse(salvarSerializa.salvarCalled);
        
        // Testar a condição if (tipoSalvar == 10)
        SerializacaoXML serializaXML = salvar.serializaXML;
        assertNotNull(serializaXML);
        
        assertEquals(salvar.entidade, serializaXML.entidades);
        
        assertEquals(salvar.arquivo + ".jaw.xml", serializaXML.arquivo);
        
        assertEquals(saveOriginalArquivo, form.getNomeArquivoSalvo());
        
        final FileWriter escrevedor = serializaXML.escrevedor;
        assertNotNull(escrevedor);
        
        assertEquals(salvar.xml, serializaXML.xml);
        
        if (escrevedor.writeCalled) {
            JOptionPane.showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
        }
        assertFalse(escrevedor.writeCalled);
    }
    
    private static class ProcessarEntidades {
        public boolean getEntidadesCalled;
        public int getNomeArquivoSalvoCalledTimes;
        public String nomeArquivoSalvo;
        
        public List<Entidade> getEntidades() {
            getEntidadesCalled = true;
            return new Vector<Entidade>();
        }
        
        public String getNomeArquivoSalvo() {
            getNomeArquivoSalvoCalledTimes++;
            return nomeArquivoSalvo;
        }
        
        public void setNomeArquivoSalvo(String nome) {
            nomeArquivoSalvo = nome;
        }
    }
    
    private static class Serializacao {
        public List<Entidade> entidades;
        public String arquivo;
        public boolean salvarCalled;
        
        public boolean salvar(List<Entidade> entidades, String arquivo) {
            this.entidades = entidades;
            this.arquivo = arquivo;
            salvarCalled = true;
            return true;
        }
    }
    
    private static class SerializacaoXML {
        public List<Entidade> entidades;
        public String arquivo;
        public FileWriter escrevedor;
        public String xml;
        
        public String entidades2XML(List<Entidade> entidades) {
            this.entidades = entidades;
            return xml;
        }
    }
    
    private static class Filtro extends FileFilter {
        @Override
        public boolean accept(final File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
        }

        @Override
        public String getDescription() {
            return "JAW files";
        }
    }
    
    private static class Filtro2 extends FileFilter {
        @Override
        public boolean accept(final File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
        }

        @Override
        public String getDescription() {
            return "XML JAW files";
        }
    }
}
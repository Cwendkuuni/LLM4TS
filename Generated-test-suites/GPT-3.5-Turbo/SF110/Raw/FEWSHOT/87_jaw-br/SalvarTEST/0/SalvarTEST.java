import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        List<Entidade> entidade = form.getEntidades();
        File currentDirectory = new File(Main.configuracao.getCaminhoDosProjetos());
        
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(currentDirectory);
        
        if (tipoSalvar == Salvar.SALVAR_XML) {
            escolhedor.setFileFilter(salvar.new Filtro2());
        }
        if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
            escolhedor.setFileFilter(salvar.new Filtro());
        }
        if (tipoSalvar == Salvar.SALVAR_XML || tipoSalvar == Salvar.SALVAR_COMO || (tipoSalvar == Salvar.SALVAR && form.getNomeArquivoSalvo().equals("Novo Projeto"))) {
            escolhedor.showSaveDialog(form);
            salvar.vela = true;
        }
        
        if (escolhedor.getSelectedFile() == null && form.getName().equals("")) {
            return;
        }
        
        if (salvar.vela) {
            if (escolhedor.getSelectedFile() == null) {
                return;
            }
            salvar.arquivo = escolhedor.getSelectedFile().getAbsolutePath();
        } else {
            salvar.arquivo = form.getNomeArquivoSalvo();
        }
        
        if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
            if (!salvar.arquivo.endsWith(".jaw") && salvar.vela) {
                form.setNomeArquivoSalvo(salvar.arquivo += ".jaw");
                salvar.vela = false;
            } else {
                salvar.arquivo = form.getNomeArquivoSalvo();
            }
            
            Serializacao salvarSerializacao = new Serializacao();
            
            try {
                if (salvarSerializacao.salvar(entidade, salvar.arquivo)) {
                    JOptionPane.showMessageDialog(form, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
                }
            } catch (Exception ex2) {
                JOptionPane.showMessageDialog(null, "N\u00c3£o foi poss\u00c3\u00advel salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
                Main.janelaPrincipal.log(ex2.toString());
            }
        }
        
        if (tipoSalvar == Salvar.SALVAR_XML) {
            if (!salvar.arquivo.endsWith(".jaw.xml")) {
                salvar.arquivo += ".jaw.xml";
            }
            
            SerializacaoXML serializaXML = new SerializacaoXML();
            salvar.xml = serializaXML.entidades2XML(entidade);
            
            try {
                FileWriter escrevedor = new FileWriter(salvar.arquivo);
                escrevedor.write(salvar.xml);
                escrevedor.flush();
                escrevedor.close();
                JOptionPane.showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(form, "N\u00c3£o foi poss\u00c3\u00advel salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
                Main.janelaPrincipal.log(e.toString());
            }
        }
    }
}
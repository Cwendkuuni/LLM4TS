import org.apache.commons.math3.ExceptionEasyBase;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        assertEquals(5, population.size());
        assertEquals(chromosome1, population.get(0));
        assertEquals(chromosome2, population.get(1));
    }

    @Test
    public void testElitisticListPopulationConstructionWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        assertEquals(3, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        new ElitisticListPopulation(3, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationElitismRateGreaterThanOne() {
        new ElitisticListPopulation(3, 1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        Chromosome chromosome4 = new DummyChromosome(4);
        Chromosome chromosome5 = new DummyChromosome(5);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);
        chromosomes.add(chromosome5);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        Collections.sort(population);

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());

        int index = 0;
        for (Chromosome chromosome : population.subList(0, 3)) {
            assertEquals(chromosome, nextGeneration.get(index++));
        }
        assertNotEquals(chromosome3, nextGeneration.get(2));
        assertNotEquals(chromosome4, nextGeneration.get(3));
        assertNotEquals(chromosome5, nextGeneration.get(4));
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        population.setElitismRate(0.3);

        assertEquals(0.3, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        population.setElitismRate(1.1);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double[] getGenes() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + id + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DummyChromosome)) {
                return false;
            }
            DummyChromosome other = (DummyChromosome) o;
            return this.id == other.id;
        }

        @Override
        public int hashCode() {
            return FastMath.hash(Double.doubleToLongBits(this.id));
        }
    }
}

package geo.google.mapping;

import geo.google.datamodel.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_typicalCase() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up the expected values
        expected.setCountry("United States");
        expected.setState("California");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("123 Main Street");
        expected.setCoordinate(new Coordinate());
        expected.setAccuracy(0.0);
        
        // Set up the input address
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode());
        addressDetails.getCountry().getCountryNameCode().get(0).setContent("United States");
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        addressDetails.getCountry().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName());
        addressDetails.getCountry().getAdministrativeArea().getAdministrativeAreaName().get(0).setContent("California");
        addressDetails.getCountry().getAdministrativeArea().setLocality(new Locality());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().get(0).setContent("90001");
        addressDetails.getCountry().getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().get(0).setContent("123 Main Street");
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_nullAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(null);
        
        assertNull(result);
    }

    @Test
    public void testExecute_missingCountry() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getCountry());
    }

    @Test
    public void testExecute_missingState() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getState());
    }

    @Test
    public void testExecute_missingCity() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getCity());
    }

    @Test
    public void testExecute_missingPostalCode() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        addressDetails.getCountry().getAdministrativeArea().setLocality(new Locality());
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getPostalCode());
    }

    @Test
    public void testExecute_missingAddressLine1() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        addressDetails.getCountry().getAdministrativeArea().setLocality(new Locality());
        addressDetails.getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getAddressLine1());
    }
    
    @Test
    public void testExecute_missingCoordinate() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        addressDetails.getCountry().getAdministrativeArea().setLocality(new Locality());
        addressDetails.getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new Thoroughfare());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().get(0).setContent("123 Main Street");
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertNull(result.getCoordinate());
    }
    
    @Test
    public void testExecute_zeroAccuracy() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        address.setAddressDetails(addressDetails);
        addressDetails.setCountry(new AddressDetails.Country());
        addressDetails.getCountry().setAdministrativeArea(new AdministrativeArea());
        addressDetails.getCountry().getAdministrativeArea().setLocality(new Locality());
        addressDetails.getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new Thoroughfare());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType());
        addressDetails.getCountry().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().get(0).setContent("123 Main Street");
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(address);
        
        assertEquals(0.0, result.getAccuracy(), 0.01);
    }
}
